export const Checkbox = ({label, checked, onChange}) => {
    return <div>
        <label>{label}</label>
        <input
            type="checkbox"
            checked={checked}
            onChange={onChange}
        />
    </div>
};
