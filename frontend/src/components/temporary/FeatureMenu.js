import {VerticalDirectedGrid} from "./VerticalDirectedGrid";
import {Box, makeStyles} from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
    grid: {
        padding: "30px",
    },
}));


export const FeatureMenu = ({children}) => {
    const classes = useStyles();

    return <Box>
        <VerticalDirectedGrid
            alignItems={"flex-end"}
        >
            <VerticalDirectedGrid
                border={1} borderRadius={5}
                width={"fit-content"}
                height={"fit-content"}
                backgroundColor={"rgba(255, 255, 255, 0.5)"}
            >
                <Box className={classes.grid}>
                    {children}
                </Box>
            </VerticalDirectedGrid>
        </VerticalDirectedGrid>
    </Box>
};
