import React from "react";
import {Box} from "@material-ui/core";

export const ServiceCard = ({title, children}) => (
    <Box
        sx={{
            height: "max-content",
            width: "250px",

            float: "left",
            margin: "0 5px",
            padding: "10px",

            background: "rgba(255, 255, 255, 0.5)",
            border: "1px solid #F88F2A",
            borderRadius: "5px",

            fontWeight: "bold",
            color: "#6F3317",
            fontSize: "20px",
            textAlign: "center",
        }}
    >
        <Box component={"p"} sx={{ fontFamily: 'foral_proextrabold' }}>
            {title}
        </Box>
        {children}
    </Box>
);
