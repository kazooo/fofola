import {useEffect} from "react";
import {useDispatch} from "react-redux";
import {makeStyles} from "@material-ui/core";

import {loadVirtualCollections} from "./saga";
import {Content} from "./Content";

const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        flexGrow: 1,
        display: 'flex',
        minHeight: '520px',
        borderRadius: '5px',
        border: '1px solid rgb(212, 101, 3)'

    },
}));

export const CheckDonator = () => {
    const classes = useStyles();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadVirtualCollections());
    }, []);

    return <Content classes={classes} />;
};
