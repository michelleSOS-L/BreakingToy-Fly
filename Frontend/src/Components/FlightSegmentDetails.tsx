type Segment = {
    departureTime: string;
    arrivalTime: string;
    airlineCode: string;
    airlineName: string;
    flightNumber: string;
    operatingCarrierCode?: string;
    operatingCarrierName?: string;
    aircraft: string;
    cabin: string;
    travelClass: string;
    amenities: {
      name: string;
      chargeable: boolean;
    }[];
  };
  
  type FlightDetailsProps = {
    segments: Segment[];
  };
  
  export default function DetailsPage({ segments }: FlightDetailsProps) {
    return (
      <div>
        <h2>Flight Segments</h2>
        {segments.map((seg, index) => (
          <div key={index} style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
            <p><strong>Departure:</strong> {seg.departureTime}</p>
            <p><strong>Arrival:</strong> {seg.arrivalTime}</p>
            <p><strong>Airline:</strong> {seg.airlineName} ({seg.airlineCode})</p>
            <p><strong>Flight Number:</strong> {seg.flightNumber}</p>
            {seg.operatingCarrierCode && (
              <p><strong>Operated by:</strong> {seg.operatingCarrierName} ({seg.operatingCarrierCode})</p>
            )}
            <p><strong>Aircraft:</strong> {seg.aircraft}</p>
            <p><strong>Cabin:</strong> {seg.cabin}</p>
            <p><strong>Class:</strong> {seg.travelClass}</p>
  
            <div>
              <strong>Amenities:</strong>
              <ul>
                {seg.amenities.map((a, idx) => (
                  <li key={idx}>
                    {a.name} {a.chargeable ? '(Chargeable)' : '(Free)'}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        ))}
      </div>
    );
  }