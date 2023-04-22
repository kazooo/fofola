import {
    Box,
    FormControl,
    Grid,
    InputLabel,
    makeStyles,
    MenuItem,
    Select,
    TextField as MuiTextField
} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {SearchButton} from '../../components/button';
import {sugoSessionRequestors} from '../constants';

import {
    getFromDateTime,
    getInternalUuid,
    getToDateTime,
    setCurrentPage,
    setFromDateTime,
    setInternalUuid,
    setToDateTime
} from './slice';
import {requestSessionPage} from './saga';
import {useTranslation} from "react-i18next";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const Filter = () => {
    const {t} = useTranslation();
    const classes = useStyles();
    const dispatch = useDispatch();

    const fromDateTime = useSelector(getFromDateTime);
    const toDateTime = useSelector(getToDateTime);
    const internalUuid = useSelector(getInternalUuid);

    const changeDateFrom = (date) => {
        dispatch(setFromDateTime(date));
    };

    const changeDateTo = (date) => {
        dispatch(setToDateTime(date));
    };

    const changeInternalUuid = (uuid) => {
        dispatch(setInternalUuid(uuid));
    };

    const searchTransitions = () => {
        dispatch(setCurrentPage(0));
        dispatch(requestSessionPage());
    };

    return (
        <Box>
        <Grid container
              alignItems={'center'}
              justifyContent={'center'}
        >
            <Grid item>
                <TextField
                    label='Z'
                    value={fromDateTime}
                    placeholder='hh:mm dd/mm/yyyy'
                    classNames={classes.formControl}
                    onChange={changeDateFrom}
                    width={170}
                />
            </Grid>
            <Grid item>
                <TextField
                    label='Do'
                    value={toDateTime}
                    placeholder='hh:mm dd/mm/yyyy'
                    classNames={classes.formControl}
                    onChange={changeDateTo}
                    width={170}
                />
            </Grid>
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
                <SearchButton onClick={searchTransitions}>
                    Hledat
                </SearchButton>
            </Grid>
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
