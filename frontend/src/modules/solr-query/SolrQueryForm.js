import {
    accesses,
    fields,
    models,
} from "./constants";
import {useDispatch, useSelector} from "react-redux";
import {requestOutputFiles, sendSolrQuery} from "./saga";
import {Box, Grid, TextField} from "@material-ui/core";
import {
    allRequiredParamsExist,
    clearParams,
    getAccess,
    getField,
    getFrom,
    getModel,
    getTo,
    setAccess,
    setField,
    setFrom,
    setModel,
    setTo
} from "./slice";
import {Selector} from "../../components/form/Selector";
import {ClearButton, StartButton} from "../../components/button";
import {useInterval} from "../../effects/useInterval";
import {useEffect} from "react";

export const SolrQueryForm = () => {

    const dispatch = useDispatch();
    const to = useSelector(state => getTo(state));
    const from = useSelector(state => getFrom(state));
    const model = useSelector(state => getModel(state));
    const access = useSelector(state => getAccess(state));
    const field = useSelector(state => getField(state));
    const ready = useSelector(state => allRequiredParamsExist(state));

    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestOutputFiles());
    }, [])

    useInterval(() => {
        dispatch(requestOutputFiles());
    }, RELOAD_INTERVAL_MS);

    const changeModel = (model) => {
        dispatch(setModel(model));
    };

    const changeAccess = (access) => {
        dispatch(setAccess(access));
    };

    const changeFrom = (from) => {
        dispatch(setFrom(from));
    };

    const changeTo = (to) => {
        dispatch(setTo(to));
    };

    const changeField = (field) => {
        dispatch(setField(field));
    };

    const submit = () => {
        if (ready) {
            dispatch(sendSolrQuery({
                "year_from": from,
                "year_to": to,
                "model": model,
                "accessibility": access,
            }));
        }
    };

    const clear = () => {
        if (ready) {
            dispatch(clearParams());
        }
    }

    return <Box>
        <Grid container
              direction="column"
              alignItems={"center"}
              justifyContent={"center"}
              spacing={3}
        >
            <Grid item>
                <TextField
                    label="Z"
                    value={from}
                    variant="outlined"
                    placeholder="dd-mm-yyyy"
                    onChange={e => changeFrom(e.target.value)}
                    size="small"
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <TextField
                    label="Do"
                    value={to}
                    variant="outlined"
                    placeholder="dd-mm-yyyy"
                    onChange={e => changeTo(e.target.value)}
                    size="small"
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel={'Model'}
                    selectOptions={models}
                    selectedOption={model}
                    onSelectOptionChange={changeModel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel={'Dostupnost'}
                    selectOptions={accesses}
                    selectedOption={access}
                    onSelectOptionChange={changeAccess}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel={'Vytvořit seznam'}
                    selectOptions={fields}
                    selectedOption={field}
                    onSelectOptionChange={changeField}
                />
            </Grid>
            {ready &&
                <Grid item>
                    <StartButton onClick={submit}>Vytvořit seznam</StartButton>
                </Grid>
            }
            {ready &&
                <Grid item>
                    <ClearButton onClick={clear}>Vyčistit</ClearButton>
                </Grid>
            }
        </Grid>
    </Box>
};
