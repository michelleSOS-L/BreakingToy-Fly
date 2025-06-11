interface Segment {
    departureTime: string;
    arrivalTime: string;
    airlineName: string;
    airlineCode: string;
    flightNumber: string;
    aircraft: string;
    cabin: string;
    class: string;
    amenities: { name: string; chargeable: boolean }[];
  }
  
  interface Props {
    segments: Segment[];
    layovers: string[];
  }
  
  export default function FlightDetails({ segments, layovers }: Props) {
    return (
      <div>
        {segments.map((segment, i) => (
          <div key={i}>
            <p>
              {segment.departureTime} → {segment.arrivalTime}
            </p>
            <p>
              {segment.airlineName} ({segment.airlineCode}) · Flight {segment.flightNumber}
            </p>
            <p>Aircraft: {segment.aircraft}</p>
            <p>
              Cabin: {segment.cabin}, Class: {segment.class}
            </p>
            <ul>
              {segment.amenities.map((a, j) => (
                <li key={j}>
                  {a.name} {a.chargeable ? '(extra cost)' : '(included)'}
                </li>
              ))}
            </ul>
          </div>
        ))}
        <h4>Layovers:</h4>
        <ul>
          {layovers.map((stop, i) => (
            <li key={i}>{stop}</li>
          ))}
        </ul>
      </div>
    );
  }