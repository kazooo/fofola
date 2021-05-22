import React from "react";
import {Box} from "@material-ui/core";

export const Tabs = ({children, value, classes}) => (
    children.map((ch, index) => (
        <Box
            key={index}
            hidden={value !== index}
            className={classes.tabs}
        >
            {value === index && ch}
        </Box>
    ))
)
