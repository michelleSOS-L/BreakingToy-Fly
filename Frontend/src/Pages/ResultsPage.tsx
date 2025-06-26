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
        // Fetch ALL results, not paginated
        const response = await fetchAPI(`/api/flights/search?${queryParams}`);
        const deduplicated = deduplicateFlights(response.data || response);
        setAllFlights(deduplicated);
        setCurrentPage(0); // reset to first page on new search
      } catch (err) {
        console.error('❌ Failed to fetch flights:', err);
        setAllFlights([]);
      } finally {
        setLoading(false);
      }
    };

    fetchFlights();
  }, [queryParams]);

  const totalPages = Math.ceil(allFlights.length / flightsPerPage);
  const currentFlights = allFlights.slice(
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
              backgroundColor: index === currentPage ? '#007bff' : '#eee',
              color: index === currentPage ? 'white' : 'black',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
            }}
          >
            {index + 1}
          </button>
        ))}
      </div>

      <button
        onClick={() => navigate('/')}
        style={{ marginTop: '1.5rem', display: 'block' }}
      >
        ⬅ Back to Search
      </button>
    </div>
  );
}