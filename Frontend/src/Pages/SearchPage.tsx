// src/pages/SearchPage.tsx
import SearchForm from '../Components/SearchForm';

export default function SearchPage() {
  return (
    <div style={{ padding: '2rem' }}>
      <h1>Search for Flights</h1>
      <SearchForm />
    </div>
  );
}