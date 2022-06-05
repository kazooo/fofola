import {useDispatch} from 'react-redux';
import {useEffect, useState} from 'react';
import {Box, Grid} from '@material-ui/core';

import {ClearButton, StartButton} from '../../components/button';
import {TextField} from '../../components/form/TextField';
import {Selector} from '../../components/form/Selector';
import {useInterval} from '../../effects/useInterval';

import {extendedAccesses, extendedModels, extendedDnntLabels, dnntFlags} from './constants';
import {ExtendedFieldValue, SolrField, solrFields} from '../constants';
import {requestOutputFiles, sendSolrQuery} from './saga';

export const SolrQueryForm = () => {

    const [from, setFrom] = useState(null);
    const [to, setTo] = useState(null);
    const [model, setModel] = useState(ExtendedFieldValue.ANY.value);
    const [access, setAccess] = useState(ExtendedFieldValue.ANY.value);
    const [dnntLabel, setDnntLabel] = useState(ExtendedFieldValue.ANY.value);
    const [dnntFlag, setDnntFlag] = useState(ExtendedFieldValue.ANY.value);
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
        setModel(ExtendedFieldValue.ANY.value);
        setAccess(ExtendedFieldValue.ANY.value);
        setDnntLabel(ExtendedFieldValue.ANY.value);
        setDnntFlag(ExtendedFieldValue.ANY.value);
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
                    label='common.form.from'
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
                    label='common.form.to'
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
                    selectLabel='feature.solrQuery.form.model'
                    selectOptions={extendedModels}
                    selectedOption={model}
                    onSelectOptionChange={setModel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='feature.solrQuery.form.accessibility'
                    selectOptions={extendedAccesses}
                    selectedOption={access}
                    onSelectOptionChange={setAccess}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='feature.solrQuery.form.dnntLabel'
                    selectOptions={extendedDnntLabels}
                    selectedOption={dnntLabel}
                    onSelectOptionChange={setDnntLabel}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='feature.solrQuery.form.dnntFlag'
                    selectOptions={dnntFlags}
                    selectedOption={dnntFlag}
                    onSelectOptionChange={setDnntFlag}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel='feature.solrQuery.form.createList'
                    selectOptions={solrFields}
                    selectedOption={field}
                    onSelectOptionChange={setField}
                />
            </Grid>
            {ready &&
                <Grid item>
                    <StartButton
                        label={'feature.solrQuery.button.createList'}
                        onClick={submit}
                    />
                </Grid>
            }
            {ready &&
                <Grid item>
                    <ClearButton
                        onClick={clear}
                    />
                </Grid>
            }
        </Grid>
    </Box>
};
