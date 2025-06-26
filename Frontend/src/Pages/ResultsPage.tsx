import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import FlightCard from '../Components/FlightCard';
import { fetchAPI } from '../api/flightService';

interface Flight {
  id: string;
  departureTime: string;
  arrivalTime: string;
  departureAirport: string;
  arrivalAirport: string;
  airlineName: string;
  airlineCode: string;
  duration: string;
  totalPrice: number;
  currency: string;
}

export default function ResultsPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const [allFlights, setAllFlights] = useState<Flight[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [queryParams, setQueryParams] = useState<string | null>(null);
  const [sortBy, setSortBy] = useState('');

  const flightsPerPage = 10;

  const deduplicateFlights = (flights: Flight[]) => {
    const seen = new Set<string>();
    return flights.filter(flight => {
      const key = `${flight.departureAirport}-${flight.arrivalAirport}-${flight.departureTime}-${flight.arrivalTime}-${flight.totalPrice}`;
      if (seen.has(key)) return false;
      seen.add(key);
      return true;
    });
  };

  useEffect(() => {
    if (!location.state?.query) {
      navigate('/');
      return;
    }

    if (!queryParams) {
      setQueryParams(location.state.query);
    }
  }, [location.state, queryParams, navigate]);

  useEffect(() => {
    if (queryParams === null) return;

    const fetchFlights = async () => {
      setLoading(true);
      try {
        const response = await fetchAPI(`/api/flights/search?${queryParams}`);
        const deduplicated = deduplicateFlights(response.data || response);
        setAllFlights(deduplicated);
        setCurrentPage(0);
      } catch (err) {
        console.error('❌ Failed to fetch flights:', err);
        setAllFlights([]);
      } finally {
        setLoading(false);
      }
    };

    fetchFlights();
  }, [queryParams]);

  const sortFlights = (flights: Flight[], sortBy: string): Flight[] => {
    const sorted = [...flights];
    switch (sortBy) {
      case 'durationAsc':
        return sorted.sort((a, b) => a.duration.localeCompare(b.duration));
      case 'durationDesc':
        return sorted.sort((a, b) => b.duration.localeCompare(a.duration));
      case 'priceAsc':
        return sorted.sort((a, b) => a.totalPrice - b.totalPrice);
      case 'priceDesc':
        return sorted.sort((a, b) => b.totalPrice - a.totalPrice);
      default:
        return flights;
    }
  };

  const sortedFlights = sortFlights(allFlights, sortBy);
  const totalPages = Math.ceil(sortedFlights.length / flightsPerPage);
  const currentFlights = sortedFlights.slice(
    currentPage * flightsPerPage,
    (currentPage + 1) * flightsPerPage
  );

  const handleCardClick = (flightId: string) => {
    navigate(`/details/${flightId}`);
  };

  if (loading) return <h2>Loading flights...</h2>;

  if (!allFlights.length) {
    return (
      <div>
        <h2>No flights found</h2>
        <button onClick={() => navigate('/')}>⬅ Go Back</button>
      </div>
    );
  }

  return (
    <div style={{ padding: '1rem' }}>
      <h1>Flight Results</h1>

      <div style={{ marginBottom: '1rem' }}>
        <label htmlFor="sortBy" style={{ marginRight: '0.5rem', fontWeight: 'bold' }}>
          Sort by:
        </label>
        <select
          id="sortBy"
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value)}
          style={{
            padding: '0.6rem',
            border: '1px solid #ccc',
            borderRadius: '6px',
            backgroundColor: '#f7f0ff',
            color: '#444',
            fontSize: '0.95rem',
            fontWeight: '500',
          }}
        >
          <option value="">Select</option>
          <option value="durationAsc"> Duration (Short → Long)</option>
          <option value="durationDesc"> Duration (Long → Short)</option>
          <option value="priceAsc"> Price (Low → High)</option>
          <option value="priceDesc">Price (High → Low)</option>
        </select>
      </div>

      {currentFlights.map((flight) => (
        <div
          key={flight.id}
          onClick={() => handleCardClick(flight.id)}
          style={{ cursor: 'pointer' }}
        >
          <FlightCard flight={flight} />
        </div>
      ))}

      <div style={{ marginTop: '1rem' }}>
        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={index}
            onClick={() => setCurrentPage(index)}
            style={{
              margin: '0.25rem',
              padding: '0.5rem 1rem',
              backgroundColor: index === currentPage ? '#cfa3f3' : '#eee',
              color: index === currentPage ? 'white' : 'black',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontWeight: 'bold',
            }}
          >
            {index + 1}
          </button>
        ))}
      </div>

      <button
        onClick={() => navigate('/')}
        style={{
          marginTop: '1.5rem',
          display: 'block',
          backgroundColor: '#e6ccfa',
          color: '#444',
          padding: '0.6rem 1rem',
          borderRadius: '6px',
          border: 'none',
          cursor: 'pointer',
        }}
      >
        ⬅ Back to Search
      </button>
    </div>
  );
}