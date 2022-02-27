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
import {
    getAccess,
    getCnb,
    getFromDateTime,
    getInternalUuid,
    getModel,
    getSourceIdentifier,
    getSourceUuid,
    getToDateTime,
    setAccess,
    setCnb,
    setCurrentPage,
    setFromDateTime,
    setInternalUuid,
    setModel,
    setSourceIdentifier,
    setSourceUuid,
    setToDateTime
} from './slice';
import {requestSessionPage} from './saga';
import {extendedAccesses, extendedModels} from './constants';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const Filter = () => {
    const classes = useStyles();
    const dispatch = useDispatch();

    const fromDateTime = useSelector(getFromDateTime);
    const toDateTime = useSelector(getToDateTime);
    const internalUuid = useSelector(getInternalUuid);
    const model = useSelector(getModel);
    const access = useSelector(getAccess);
    const cnb = useSelector(getCnb);
    const sourceIdentifier = useSelector(getSourceIdentifier);
    const sourceUuid = useSelector(getSourceUuid);

    const createOptions = options => options.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const modelOptions = createOptions(extendedModels);
    const accessOptions = createOptions(extendedAccesses);

    const changeDateFrom = (date) => {
        dispatch(setFromDateTime(date));
    };

    const changeDateTo = (date) => {
        dispatch(setToDateTime(date));
    };

    const changeInternalUuid = (uuid) => {
        dispatch(setInternalUuid(uuid));
    };

    const changeModel = (model) => {
        dispatch(setModel(model));
    };

    const changeAccess = (access) => {
        dispatch(setAccess(access));
    };

    const changeCnb = (cnb) => {
        dispatch(setCnb(cnb));
    };

    const changeSourceIdentifier = (sourceIdentifier) => {
        dispatch(setSourceIdentifier(sourceIdentifier));
    };

    const changeSourceUuid = (uuid) => {
        dispatch(setSourceUuid(uuid));
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
                <SelectorField
                    label='Model'
                    value={model}
                    onChange={changeModel}
                    options={modelOptions}
                    classNames={classes.formControl}
                />
            </Grid>
            <Grid item>
                <SelectorField
                    label='Dostupnost'
                    value={access}
                    onChange={changeAccess}
                    options={accessOptions}
                    classNames={classes.formControl}
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
                    label='Zdrojový ID'
                    value={sourceIdentifier}
                    placeholder='OAI:...'
                    classNames={classes.formControl}
                    onChange={changeSourceIdentifier}
                    width={120}
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
            inputProps={{ maxLength: 40 }}
            style={{ width: width }}
        />
    </FormControl>
);

const SelectorField = ({label, value, onChange, options, classNames}) => (
    <FormControl variant='outlined' size='small' className={classNames}>
        <InputLabel>{label}</InputLabel>
        <Select
            value={value}
            onChange={e => onChange(e.target.value)}
            label={label}
        >
            {options}
        </Select>
    </FormControl>
);
