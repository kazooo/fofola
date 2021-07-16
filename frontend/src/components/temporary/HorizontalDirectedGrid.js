import {Box, Grid} from "@material-ui/core";
import React from "react";

export const HorizontalDirectedGrid = ({
                                           children,
                                           alignItems = "center",
                                           spacing = 3,
                                           justifyContent = "center",
                                       }) => {

    const childArray = React.Children.toArray(children);
    const gridItems = childArray.map((children, index) => (
            <Grid item key={index}>
                {children}
            </Grid>
        )
    );

    return <Box>
        <Grid container
              direction="row"
              alignItems={alignItems}
              justifyContent={justifyContent}
              spacing={spacing}
        >
            {gridItems}
        </Grid>
    </Box>
};
