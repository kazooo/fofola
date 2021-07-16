import {Box, createTheme, ThemeProvider} from "@material-ui/core";
import {Navbar} from "../navbar/Navbar";

const theme = createTheme({
    typography: {
        fontFamily: [
            'Avenir Book',
            'serif',
        ].join(','),
    },}
);

export const FofolaPage = (props) => (
    <ThemeProvider theme={theme}>
        <Box>
            <Navbar />
            {props.children}
        </Box>
    </ThemeProvider>
);
