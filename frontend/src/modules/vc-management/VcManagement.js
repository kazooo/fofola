import {useEffect} from "react";
import {useDispatch} from "react-redux";
import {makeStyles} from "@material-ui/core";

import {HorizontallyCenteredBox} from "../../components/temporary/HorizontallyCenteredBox";
import {FofolaPage} from "../../components/temporary/FofolaPage";

import {loadVirtualCollections} from "./saga";
import {Panel} from "./Panel";

const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        flexGrow: 1,
        display: 'flex',
        minHeight: '520px',
        borderRadius: '5px',
        border: '1px solid rgb(212, 101, 3)'

    },
    options: {
        borderRight: `1px solid ${theme.palette.divider}`,
    },
    tabs: {
        width: '100%',
    }
}));

export const VcManagement = () => {
    const classes = useStyles();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadVirtualCollections());
    }, []);
    
    return (
        <FofolaPage>
            <HorizontallyCenteredBox width={'70%'}>
                <Panel classes={classes} />
            </HorizontallyCenteredBox>
        </FofolaPage>
    );
};
