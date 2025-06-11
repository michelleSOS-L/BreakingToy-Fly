import { useParams } from 'react-router-dom';
import FlightDetails from '../Components/FlightDetails';
import PriceBreakdown from '../Components/PriceBreakdown';
import FlightSegmentDetails from '../Components/FlightSegmentDetails'; // NEW

export default function DetailsPage() {
  const { id } = useParams();

  const mockFlightData = {
    id,
    segments: [
      {
        departureTime: '2025-08-01T12:30',
        arrivalTime: '2025-08-01T16:00',
        airlineName: 'SkyAir',
        airlineCode: 'SA',
        flightNumber: 'SA123',
        aircraft: 'Boeing 737',
        cabin: 'Economy',
        travelClass: 'Y',
        amenities: [
          { name: 'WiFi', chargeable: true },
          { name: 'Meal', chargeable: false },
        ],
      },
    ],
    layovers: ['45 minutes at JFK'],
    price: {
      base: 300,
      fees: 50,
      total: 350,
      perTraveler: 350,
      currency: 'USD',
    },
  };

  return (
    <div>
      <h2>Flight Details</h2>
      <FlightSegmentDetails segments={mockFlightData.segments} /> {/* WAS: <DetailsPage /> */}
      <FlightDetails segments={mockFlightData.segments} layovers={mockFlightData.layovers} />
      <PriceBreakdown price={mockFlightData.price} />
    </div>
  );
}