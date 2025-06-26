import AsyncSelect from 'react-select/async';

interface Option {
  label: string;
  value: string;
}

interface Props { 
  label: string;
  value: string;
  onChange: (code: string, name: string) => void;
}

export default function AutocompleteInput({ label, value, onChange }: Props) {
  const loadOptions = async (inputValue: string): Promise<Option[]> => {
    if (!inputValue || inputValue.length < 2) return [];

    try {
      const res = await fetch(`http://localhost:9090/api/airports/search?keyword=${inputValue}`);
      const data = await res.json();

      return data.map((loc: any) => ({
        label: `${loc.fullName} (${loc.iataCode})`,
        value: loc.iataCode,
      }));
    } catch (err) {
      console.error('Failed to fetch suggestions:', err);
      return [];
    }
  };

  return (
    <div style={{ marginBottom: '1rem' }}>
      <label style={{ display: 'block', marginBottom: '0.5rem' }}>{label}</label>
      <AsyncSelect
        cacheOptions
        defaultOptions
        loadOptions={loadOptions}
        placeholder="Airport..."
        onChange={(selected) => {
          if (selected) onChange(selected.value, selected.label);
        }}
        value={value ? { label: value, value } : null}
      />
    </div>
  );
}