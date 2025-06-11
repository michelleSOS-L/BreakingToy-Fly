import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AutocompleteInput from '../Components/AutoCompleteInput';

export default function SearchForm() {
  const navigate = useNavigate();

  const [departure, setDeparture] = useState('');
  const [arrival, setArrival] = useState('');
  const [departureDate, setDepartureDate] = useState('');
  const [returnDate, setReturnDate] = useState('');
  const [adults, setAdults] = useState(1);
  const [currency, setCurrency] = useState('USD');
  const [nonStop, setNonStop] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const query = new URLSearchParams({
      origin: departure,
      destination: arrival,
      date: departureDate,
      adults: adults.toString(),
      currency,
      nonStop: nonStop.toString(),
    }).toString();

    try {
      const response = await fetch(`http://localhost:9090/api/airports/flights?${query}`);
      if (!response.ok) throw new Error('Error fetching flights');

      const data = await response.json();
      navigate('/results', { state: { flights: data } });
    } catch (error) {
      console.error('Failed to fetch flights:', error);
      alert('Something went wrong fetching flights.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
  Departure Airport:
  <input value={departure} onChange={(e) => setDeparture(e.target.value)} required />
</label>

<label>
  Arrival Airport:
  <input value={arrival} onChange={(e) => setArrival(e.target.value)} required />
</label>


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
        Number of Adults:
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

      <button type="submit">Search Flights</button>
    </form>
  );
}