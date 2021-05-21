import "./style.css";

export const Selector = ({label, options, onChange}) => {

    const handleOnChange = (e) => {
        e.preventDefault();
        onChange(e.target.value);
    };

    return <form className="form-inline">
        <label>{label}</label>
        <select onChange={handleOnChange}>
            {options.map(option => <option value={option.value}>{option.text}</option>)}
        </select>
    </form>;
}
