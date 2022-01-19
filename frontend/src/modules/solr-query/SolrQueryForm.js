import {useDispatch} from 'react-redux';
import {useEffect, useState} from 'react';
import {Box, Grid, TextField} from '@material-ui/core';

import {extendedAccesses, extendedModels, extendedDnntLabels, ExtendedSolrFieldValue, dnntFlags} from "./constants";
import {ClearButton, StartButton} from '../../components/button';
import {requestOutputFiles, sendSolrQuery} from './saga';
import {Selector} from '../../components/form/Selector';
import {useInterval} from '../../effects/useInterval';
import {SolrField, solrFields} from '../constants';

export const SolrQueryForm = () => {

    const [from, setFrom] = useState(null);
    const [to, setTo] = useState(null);
    const [model, setModel] = useState(ExtendedSolrFieldValue.ANY.value);
    const [access, setAccess] = useState(ExtendedSolrFieldValue.ANY.value);
    const [dnntLabel, setDnntLabel] = useState(ExtendedSolrFieldValue.ANY.value);
    const [dnntFlag, setDnntFlag] = useState(ExtendedSolrFieldValue.ANY.value);
    const [field, setField] = useState(SolrField.UUID.value);

    const ready = from !== null && to !== null;

    const dispatch = useDispatch();

    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestOutputFiles());
    }, [dispatch])

    useInterval(() => {
        dispatch(requestOutputFiles());
    }, RELOAD_INTERVAL_MS);

    const submit = () => {
        if (ready) {
            dispatch(sendSolrQuery({
                'yearFrom': from,
                'yearTo': to,
                'model': model,
                'access': access,
                'dnntLabel': dnntLabel,
                'dnntFlag': dnntFlag,
                'field': field,
            }));
        }
    };

    const clear = () => {
        setFrom(null);
        setTo(null);
        setModel(ExtendedSolrFieldValue.ANY.value);
        setAccess(ExtendedSolrFieldValue.ANY.value);
        setDnntLabel(ExtendedSolrFieldValue.ANY.value);
        setDnntFlag(ExtendedSolrFieldValue.ANY.value);
        setField(SolrField.UUID.value);
    }

    return <Box>
        <Grid container
              direction='column'
              alignItems='center'
              justifyContent='center'
              spacing={3}
        >
            <Grid item>
                <TextField
                    label='Z'
                    value={from}
                    variant='outlined'
                    placeholder='yyyy or *'
                    onChange={e => setFrom(e.target.value)}
                    size='small'
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <TextField
                    label='Do'
                    value={to}
                    variant='outlined'
                    placeholder='yyyy or *'
                    onChange={e => setTo(e.target.value)}
                    size='small'
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='Model'
                    selectOptions={extendedModels}
                    selectedOption={model}
                    onSelectOptionChange={setModel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='Dostupnost'
                    selectOptions={extendedAccesses}
                    selectedOption={access}
                    onSelectOptionChange={setAccess}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='DNNT label'
                    selectOptions={extendedDnntLabels}
                    selectedOption={dnntLabel}
                    onSelectOptionChange={setDnntLabel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='DNNT flag'
                    selectOptions={dnntFlags}
                    selectedOption={dnntFlag}
                    onSelectOptionChange={setDnntFlag}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='Vytvořit seznam'
                    selectOptions={solrFields}
                    selectedOption={field}
                    onSelectOptionChange={setField}
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
