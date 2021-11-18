import React, {useState} from "react";
import {Box} from "@material-ui/core";
import {useSelector} from "react-redux";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {LoadingComponent} from "../../components/page/LoadingComponent";
import {Error} from "../../components/page/Error";

import {getIsLoading, getIsLoadingError} from "./slice";
import {CreateForm} from "./CreateForm";
import {EditForm} from "./EditForm";
import {Options} from "./Options";
import {Tabs} from "./Tabs";

export const Panel = ({classes}) => {
    const [tabNum, setTabNum] = useState(0);
    const isLoading = useSelector(state => getIsLoading(state));
    const isLoadingError = useSelector(state => getIsLoadingError(state));

    const handleChange = (event, newValue) => {
        setTabNum(newValue);
    };

    const panel = (
        <>
            <Options classes={classes} value={tabNum} handleChange={handleChange}>
                <span>Založit virtuální sbírku</span>
                <span>Editovat virtuální sbírku</span>
            </Options>
            <Tabs value={tabNum} classes={classes}>
                <CreateForm />
                <EditForm />
            </Tabs>
        </>
    );

    const loading = (
        <HorizontallyCenteredBox>
            <LoadingComponent label={'Načítám virtuální sbírky...'} />
        </HorizontallyCenteredBox>
    );

    const error = (
        <HorizontallyCenteredBox>
            <Error label={'Chyba při načtení virtuálních sbírek!'} />
        </HorizontallyCenteredBox>
    );

    return <Box className={classes.root}>
        {
            isLoading && loading
        }
        {
            isLoadingError && error
        }
        {
            !isLoading && !isLoadingError && panel
        }
    </Box>;
};


