import {HorizontallyCenteredBox} from "./HorizontallyCenteredBox";
import {Box} from "@material-ui/core";
import React from "react";

export const CenteredBox = ({classes, children}) => (
    <HorizontallyCenteredBox width={'70%'}>
        <Box className={classes}>
            <HorizontallyCenteredBox>
                {children}
            </HorizontallyCenteredBox>
        </Box>
    </HorizontallyCenteredBox>
);
