export const TextForm = ({label, size, placeholder, onChange}) => (
    <div>
        <label style={{margin: "5px 10px 5px 0"}}>{label}</label>
        <input
            type="text"
            size={size}
            placeholder={placeholder}
            onChange={e => onChange(e.target.value)}
        />
    </div>
);
