import {Box, makeStyles} from "@material-ui/core";

const createStyles = (width) => makeStyles({
    centered: {
        margin: 'auto',
        width: width,
        padding: '10px',
    },
})();

export const HorizontallyCenteredBox = ({children, width = '100%'}) => {
    const classes = createStyles(width);

    return <Box className={classes.centered}>
      {children}
    </Box>;
};
