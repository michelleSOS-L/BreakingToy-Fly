import { useEffect, useState } from 'react';

interface Option {
  name: string;
  iataCode: string;
}

interface Props {
  label: string;
  value: string;
  onChange: (code: string) => void;
}

export default function AutocompleteInput({ label, value, onChange }: Props) {
  const [suggestions, setSuggestions] = useState<Option[]>([]);
  const [query, setQuery] = useState(value);
  const [showDropdown, setShowDropdown] = useState(false);

  useEffect(() => {
    if (query.length < 2) return;

    const timeout = setTimeout(() => {
      fetch(`http://localhost:9090/api/airports?keyword=${query}`)
        .then((res) => res.json())
        .then((data) => {
          const results = data.data.map((loc: any) => ({
            name: loc.detailedName,
            iataCode: loc.iataCode,
          }));
          setSuggestions(results);
          setShowDropdown(true);
        })
        .catch((err) => {
          console.error('Failed to fetch suggestions:', err);
        });
    }, 300); // debounce input

    return () => clearTimeout(timeout);
  }, [query]);

  const handleSelect = (option: Option) => {
    onChange(option.iataCode);
    setQuery(option.name); 
    setShowDropdown(false);
  };

  return (
    <div style={{ position: 'relative' }}>
      <label>{label}</label>
      <input
        value={query}
        onChange={(e) => {
          setQuery(e.target.value);
          setShowDropdown(true);
        }}
        required
      />
      {showDropdown && suggestions.length > 0 && (
        <ul
          style={{
            position: 'absolute',
            background: 'white',
            listStyle: 'none',
            padding: '0',
            margin: '0',
            border: '1px solid #ccc',
            width: '100%',
            zIndex: 10,
          }}
        >
          {suggestions.map((option) => (
            <li
              key={option.iataCode}
              onClick={() => handleSelect(option)}
              style={{ padding: '5px', cursor: 'pointer' }}
            >
              {option.name} ({option.iataCode})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
