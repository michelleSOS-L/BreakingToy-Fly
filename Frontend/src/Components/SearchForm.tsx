import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AutocompleteInput from './AutoCompleteInput';
import { fetchAPI } from '../api/flightService';

export default function SearchForm() {
  const navigate = useNavigate();

  const [departure, setDeparture] = useState('');
  const [arrival, setArrival] = useState('');
  const [departureDate, setDepartureDate] = useState('');
  const [returnDate, setReturnDate] = useState('');
  const [adults, setAdults] = useState(1);
  const [currency, setCurrency] = useState('USD');
  const [nonStop, setNonStop] = useState(false);
  const [sortBy, setSortBy] = useState('price');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const today = new Date().toISOString().split('T')[0]; // format: yyyy-mm-dd

    if (!departure || !arrival) {
      alert('Please select both departure and arrival airports.');
      return;
    }

    if (!departureDate) {
      alert('Please choose a departure date.');
      return;
    }

    if (departureDate < today) {
      alert('Departure date cannot be in the past.');
      return;
    }

    if (returnDate) {
      if (returnDate === departureDate) {
        alert('Departure and return dates cannot be the same.');
        return;
      }
      if (returnDate < departureDate) {
        alert('Return date cannot be before departure date.');
        return;
      }
    }

    if (adults < 1) {
      alert('Number of adults must be at least 1.');
      return;
    }

    const queryParams = new URLSearchParams({
      originLocationCode: departure,
      destinationLocationCode: arrival,
      departureDate,
      returnDate,
      adults: adults.toString(),
      currency,
      nonStop: nonStop.toString(),
      sortBy,
    });

    try {
      const data = await fetchAPI(`/api/flights/search?${queryParams.toString()}`);
      navigate('/results', {
        state: { query: queryParams.toString() }
      });
    } catch (error) {
      console.error(' Error fetching flights:', error);
      alert('Something went wrong fetching flights.');
    }
  };

  return (
    <form className="search-form" onSubmit={handleSubmit}>
      <AutocompleteInput
        label="Departure Airport:"
        value={departure}
        onChange={(code, fullName) => setDeparture(code)}
      />

      <AutocompleteInput
        label="Arrival Airport:"
        value={arrival}
        onChange={(code, fullName) => setArrival(code)}
      />

      <label>
        Departure Date:
        <input
          type="date"
          value={departureDate}
          onChange={(e) => setDepartureDate(e.target.value)}
          required
        />
      </label>

      <label>
        Return Date:
        <input
          type="date"
          value={returnDate}
          onChange={(e) => setReturnDate(e.target.value)}
        />
      </label>

      <label>
        Adults:
        <input
          type="number"
          value={adults}
          onChange={(e) => setAdults(parseInt(e.target.value))}
          min={1}
          required
        />
      </label>

      <label>
        Currency:
        <select value={currency} onChange={(e) => setCurrency(e.target.value)}>
          <option value="USD">USD</option>
          <option value="MXN">MXN</option>
          <option value="EUR">EUR</option>
        </select>
      </label>

      <label>
        <input
          type="checkbox"
          checked={nonStop}
          onChange={(e) => setNonStop(e.target.checked)}
        />
        Non-stop only
      </label>

      <label>
        Sort By:
        <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
          <option value="price">Price</option>
          <option value="duration">Duration</option>
        </select>
      </label>

      <button type="submit">Search Flights</button>
    </form>
  );
}