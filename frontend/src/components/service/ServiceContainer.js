import React from "react";

export const ServiceContainer = (props) => (
    <div style={{
        paddingTop: "12%",
        paddingRight: "45%",
        textAlign: "center",
    }}>
        <h1 style={{
            fontFamily: "foral_proextrabold, serif",
            color: "#6F3317",
            textTransform: "uppercase"
        }}>{props.title}</h1>

        <div style={{
            padding: "30px",
            width: "max-content",
            margin: "auto"
        }}>
            {props.children}
        </div>
    </div>
);
