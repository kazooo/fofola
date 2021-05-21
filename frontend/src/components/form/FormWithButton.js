import {useState} from "react";
import './style.css';

export const FormWithButton = ({type, size, label, placeholder, button, submitFunc}) => {

    const [text, setText] = useState("");

    const handleOnClick = (e) => {
        e.preventDefault();
        if (text)
            submitFunc(text);
    }

    return <form className="form-inline">
        <label style={{margin: "5px 10px 5px 0"}}>{label}</label>
        <input
            type={type}
            size={size}
            value={text}
            onChange={e => setText(e.target.value)}
            style={{
                verticalAlign: "middle",
                margin: "5px 10px 5px 0",
                padding: "10px",
                backgroundColor: "#fff",
                border: "1px solid #ddd"
            }}
            placeholder={placeholder}
        />
        <button type="submit" onClick={handleOnClick}>{button}</button>
    </form>;
}
