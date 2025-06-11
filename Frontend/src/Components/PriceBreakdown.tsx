interface Price {
    base: number;
    fees: number;
    total: number;
    perTraveler: number;
    currency: string;
  }
  
  export default function PriceBreakdown({ price }: { price: Price }) {
    return (
      <div>
        <h4>Price Breakdown</h4>
        <p>Base Price: {price.base} {price.currency}</p>
        <p>Fees: {price.fees} {price.currency}</p>
        <p>Total: {price.total} {price.currency}</p>
        <p>Per Traveler: {price.perTraveler} {price.currency}</p>
      </div>
    );
  }