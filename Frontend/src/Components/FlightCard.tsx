type FlightCardProps = {
    flight: {
      id: string;
      departureTime: string;
      arrivalTime: string;
      departureAirport: string;
      arrivalAirport: string;
      airline: string;
      totalDuration: string;
      price: string;
    };
  };
  
  export default function FlightCard({ flight }: FlightCardProps) {
    return (
      <div>
        <h3>{flight.airline}</h3>
        <p>{flight.departureAirport} → {flight.arrivalAirport}</p>
        <p>{flight.departureTime} → {flight.arrivalTime}</p>
        <p>Duration: {flight.totalDuration}</p>
        <p>Price: {flight.price}</p>
      </div>
    );
  }
  