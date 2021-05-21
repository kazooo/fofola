import React from "react";

export const ServiceCard = (props) => (
    <div style={{
        background: "rgba(255, 255, 255, 0.5)",

        float: "left",
        margin: "0 5px",
        border: "1px solid #F88F2A",
        borderRadius: "5px",
        padding: "10px",
        height: "max-content",

        fontWeight: "bold",
        color: "#6F3317",
        fontSize: "20px",
        width: "250px"
    }}>
        <p style={{fontFamily: 'foral_proextrabold'}}>{props.title}</p>
        {props.children}
    </div>
);
