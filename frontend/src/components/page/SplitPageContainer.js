import {Box, Grid} from "@material-ui/core";

export const SplitPageContainer = ({
                                       leftSide,
                                       rightSide,
                                       alignItems = 'center',
                                       direction = 'row',
                                       leftSideColumns = 6,
                                       rightSideColumns = 6
}) => (
    <Box>
        <Grid container direction={direction} alignItems={alignItems}>
            <Grid item xs={leftSideColumns}>
                {leftSide}
            </Grid>
            <Grid item xs={rightSideColumns}>
                {rightSide}
            </Grid>
        </Grid>
    </Box>
);
