import FlightCard from '../Components/FlightCard';
import {useLocation, useNavigate} from 'react-router-dom'
export default function ResultsPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const flights = location.state?.flights;
  
    if (!flights || flights.length === 0) {
      return (
        <div>
          <h1>No flights found</h1>
          <button onClick={() => navigate('/')}>Go Back</button>
        </div>
      );
    }
  
    return (
      <div>
        <h1>Flight Results</h1>
        {flights.map((flight: any) => (
          <FlightCard key={flight.id} flight={flight} />
        ))}
      </div>
    );
  }