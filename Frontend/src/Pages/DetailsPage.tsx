import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import '../Styles/FlightDetails.css';

interface Amenity {
  name: string;
  chargeable: boolean;
}

interface Segment {
  id: string;
  departure: { iataCode: string; at: string };
  arrival: { iataCode: string; at: string };
  duration?: string;
  carrierCode: string;
  flightNumber: string;
  operatingCarrierCode?: string;
  operatingCarrierName?: string;
  aircraft?: string;
  cabin?: string;
  travelClass?: string;
  amenities?: Amenity[];
}

interface FlightData {
  id: string;
  segments: Segment[];
  returnSegments?: Segment[];
  departureTime: string;
  arrivalTime: string;
  departureAirport: string;
  arrivalAirport: string;
  airlineName: string;
  airlineCode: string;
  operatingAirlineName?: string;
  operatingAirlineCode?: string;
  duration: string;
  totalPrice: number;
  basePrice: number;
  fees: number;
  pricePerAdult: number;
  currency: string;
}

export default function DetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [flight, setFlight] = useState<FlightData | null>(null);

  useEffect(() => {
    fetch(`http://localhost:9090/api/flights/${id}`)
      .then((res) => res.json())
      .then(setFlight)
      .catch((err) => console.error('❌ Failed to fetch:', err));
  }, [id]);

  if (!flight) return <div className="loading">Loading...</div>;

  return (
    <div className="details-layout">
      <div className="left-panel">
        {[...flight.segments, ...(flight.returnSegments || [])].map((seg, index) => (
          <div key={seg.id} className="segment-card">
            <div>
              <p><strong>Segment {index + 1}</strong></p>
              <p>{seg.departure.at} → {seg.arrival.at}</p>
              <p>{seg.departure.iataCode} → {seg.arrival.iataCode}</p>
              <p>{flight.airlineName} ({flight.airlineCode}) {seg.flightNumber}</p>
              {seg.operatingCarrierCode && (
                <p>Operated by: {seg.operatingCarrierName} ({seg.operatingCarrierCode})</p>
              )}
            </div>

            <div className="fare-box">
            <p>Cabin: {seg.cabin || 'Not specified'}</p>
            <p>Class: {seg.travelClass || 'Not specified'}</p>
              <div>
                Amenities:
                <ul>
                  {seg.amenities?.length ? seg.amenities.map((a, i) => (
                    <li key={i}>{a.name} {a.chargeable ? '(Chargeable)' : '(Free)'}</li>
                  )) : <li>No amenities listed</li>}
                </ul>
              </div>
            </div>
          </div>
        ))}

        <button className="return-button" onClick={() => navigate('/')}>⬅ Return to Search</button>
      </div>

      <div className="right-panel">
        <div className="price-box">
          <h4>Price Breakdown</h4>
          <p>Base: {flight.basePrice} {flight.currency}</p>
          <p>Fees: {flight.fees} {flight.currency}</p>
          <p><strong>Total: {flight.totalPrice} {flight.currency}</strong></p>
        </div>

        <div className="per-traveler-box">
          <h4>Per Traveler</h4>
          <p>{flight.pricePerAdult} {flight.currency}</p>
        </div>
      </div>
    </div>
  );
}

