import { useNavigate } from 'react-router-dom';
import '../Styles/FlightCard.css';

type FlightCardProps = {
  flight: {
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
    pricePerTraveler?: number;
    stops?: number;
    layovers?: string[];
  };
};

export default function FlightCard({ flight }: FlightCardProps) {
  const navigate = useNavigate();

  return (
    <div className="flight-card" onClick={() => navigate(`/details/${flight.id}`)}>
      <div className="flight-left">
        <p className="flight-time">
          {new Date(flight.departureTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} -{' '}
          {new Date(flight.arrivalTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
        </p>
        <p className="flight-route">{flight.departureAirport} → {flight.arrivalAirport}</p>
        <p className="flight-duration">
          {flight.duration} {flight.stops ? `(${flight.stops} stop${flight.stops > 1 ? 's' : ''})` : '(Nonstop)'}
        </p>
        {Array.isArray(flight.layovers) && flight.layovers.length > 0 && (
          <p className="flight-layover">{flight.layovers.join(' • ')}</p>
        )}
        <p className="flight-airline">{flight.airlineName} ({flight.airlineCode})</p>
      </div>

      <div className="flight-right">
        <p className="flight-price-total">{flight.totalPrice.toFixed(2)} {flight.currency} total</p>
        <p className="flight-price-per">{
          flight.pricePerTraveler?.toFixed(2) ?? (flight.totalPrice / 3).toFixed(2)
        } {flight.currency} per Traveler</p>
      </div>
    </div>
  );
}
