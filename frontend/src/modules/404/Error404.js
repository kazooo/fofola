import {HorizontallyCenteredBox} from "../../components/temporary/HorizontallyCenteredBox";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {Box, makeStyles} from "@material-ui/core";
import {Error} from "../../components/temporary/Error";
import React from "react";

const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        flexGrow: 1,
        display: 'flex',
        minHeight: '520px',
        borderRadius: '5px',
        border: '1px solid rgb(212, 101, 3)'

    }
}));

export const Error404 = () => {
    const classes = useStyles();

    return <FofolaPage>
        <HorizontallyCenteredBox width={'70%'}>
            <Box className={classes.root}>
                <HorizontallyCenteredBox>
                    <Error label={'Stránka nebyla nalezena!'} />
                </HorizontallyCenteredBox>
            </Box>
        </HorizontallyCenteredBox>
    </FofolaPage>
};
