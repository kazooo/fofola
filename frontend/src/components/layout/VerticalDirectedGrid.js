import React from "react";
import {Box, Grid} from "@material-ui/core";

export const VerticalDirectedGrid = ({
                                  children,
                                  border = 0,
                                  spacing = 0,
                                  borderRadius = 0,
                                  width = "inherited",
                                  height = "inherited",
                                  alignItems = "center",
                                  backgroundColor = "none",
                              }) => {

    const childArray = React.Children.toArray(children);
    const gridItems = childArray.map((children, index) => (
            <Grid item key={index}>
                {children}
            </Grid>
        )
    )

    return <Box
        border={border}
        borderRadius={borderRadius}
        width={width}
        height={height}
        style={{
            background: backgroundColor
        }}
    >
        <Grid container
              direction="column"
              spacing={spacing}
              alignItems={alignItems}
        >
            {gridItems}
        </Grid>
    </Box>
};
