import {useDispatch} from 'react-redux';
import {useEffect, useState} from 'react';
import {Box, Grid, TextField} from '@material-ui/core';

import {accesses, dnntLabels, models, SolrField, solrFields} from '../constants';
import {ClearButton, StartButton} from '../../components/button';
import {requestOutputFiles, sendSolrQuery} from './saga';
import {Selector} from '../../components/form/Selector';
import {useInterval} from '../../effects/useInterval';

export const SolrQueryForm = () => {

    const [from, setFrom] = useState(null);
    const [to, setTo] = useState(null);
    const [model, setModel] = useState(null);
    const [access, setAccess] = useState(null);
    const [dnntLabel, setDnntLabel] = useState(null);
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
                'field': field,
            }));
        }
    };

    const clear = () => {
        setFrom(null);
        setTo(null);
        setModel(null);
        setAccess(null);
        setDnntLabel(null);
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
                    placeholder='yyyy'
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
                    placeholder='yyyy'
                    onChange={e => setTo(e.target.value)}
                    size='small'
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='Model'
                    selectOptions={models}
                    selectedOption={model}
                    onSelectOptionChange={setModel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='Dostupnost'
                    selectOptions={accesses}
                    selectedOption={access}
                    onSelectOptionChange={setAccess}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='DNNT label'
                    selectOptions={dnntLabels}
                    selectedOption={dnntLabel}
                    onSelectOptionChange={setDnntLabel}
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
