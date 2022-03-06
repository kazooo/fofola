import {Box, FormControl, Grid, makeStyles, TextField as MuiTextField} from '@material-ui/core';
import {useDispatch} from 'react-redux';
import {useState} from 'react';

import {ClearButton, SearchButton} from '../../components/button';

import {LoadStringsFromFileForm} from '../../components/form/LoadStringsFromFileForm';
import {requestInfoPage} from './saga';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
    formButton: {
        margin: theme.spacing(1),
    },
}));

export const Filter = () => {
    const classes = useStyles();
    const dispatch = useDispatch();

    const [internalUuid, setInternalUuid] = useState('');
    const [sourceUuid, setSourceUuid] = useState('');
    const [cnb, setCnb] = useState('');
    const dirtyForm = internalUuid !== '' || cnb !== '' || sourceUuid !== '';

    const changeInternalUuid = (uuid) => {
        setInternalUuid(uuid);
    };

    const changeCnb = (cnb) => {
        setCnb(cnb);
    };

    const changeSourceUuid = (uuid) => {
        setSourceUuid(uuid);
    };

    const searchSingleInfo = () => {
        dispatch(requestInfoPage({
            internalUuids: [internalUuid],
            cnb,
            sourceUuid,
        }));
    };

    const searchMultipleInfo = uuids => {
        dispatch(requestInfoPage({
            internalUuids: uuids,
        }));
    };

    const clear = () => {
        setInternalUuid('');
        setSourceUuid('');
        setCnb('');
    };

    return (
        <Box>
            <Grid container
                  alignItems={'center'}
                  justifyContent={'center'}
            >
                <Grid item>
                    <TextField
                        label='Interní UUID'
                        value={internalUuid}
                        placeholder='uuid:...'
                        classNames={classes.formControl}
                        onChange={changeInternalUuid}
                    />
                </Grid>
                <Grid item>
                    <TextField
                        label='ČNB'
                        value={cnb}
                        placeholder='cnbXXXXX'
                        classNames={classes.formControl}
                        onChange={changeCnb}
                        width={130}
                    />
                </Grid>
                <Grid item>
                    <TextField
                        label='Zdrojový UUID'
                        value={sourceUuid}
                        placeholder='uuid:...'
                        classNames={classes.formControl}
                        onChange={changeSourceUuid}
                    />
                </Grid>
                <Grid item className={classes.formButton}>
                    <SearchButton onClick={searchSingleInfo}>
                        Hledat
                    </SearchButton>
                </Grid>
                <Grid item className={classes.formButton}>
                    <LoadStringsFromFileForm
                        label="Vyberte soubor s UUID"
                        submitFunc={searchMultipleInfo}
                    />
                </Grid>
                {
                    dirtyForm && (
                        <Grid item className={classes.formButton}>
                            <ClearButton onClick={clear}>
                                Vyčistit
                            </ClearButton>
                        </Grid>
                    )
                }
            </Grid>
        </Box>
    )
};

const TextField = ({label, value, classNames, onChange, placeholder, width = 200}) => (
    <FormControl variant='outlined' size='small' className={classNames}>
        <MuiTextField
            label={label}
            value={value}
            variant='outlined'
            placeholder={placeholder}
            onChange={e => onChange(e.target.value)}
            size='small'
            inputProps={{ maxLength: 150 }}
            style={{ width: width }}
        />
    </FormControl>
);
