import {
    Box,
    FormControl,
    Grid,
    InputLabel, makeStyles,
    MenuItem,
    Select,
    TextField
} from '@material-ui/core';
import {useDispatch, useSelector} from 'react-redux';

import {
    getDirection,
    getFromDateTime,
    getOperation,
    getRequestor,
    getStatus,
    getToDateTime,
    setCurrentPage,
    setDirection,
    setFromDateTime,
    setOperation,
    setRequestor,
    setStatus,
    setToDateTime
} from './slice';
import {SearchButton} from '../../components/button';
import {requestSessionPage} from './saga';
import {
    sugoSessionDirections,
    sugoSessionOperations,
    sugoSessionRequestors,
    sugoSessionStatuses} from './constants';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    }
}));

export const Filter = () => {
    const classes = useStyles();
    const dispatch = useDispatch();
    const requestor = useSelector(getRequestor);
    const direction = useSelector(getDirection);
    const operation = useSelector(getOperation);
    const status = useSelector(getStatus);
    const fromDateTime = useSelector(getFromDateTime);
    const toDateTime = useSelector(getToDateTime);

    const createOptions = options => options.map((option, index) => (
        <MenuItem key={index} value={option.value}>
            {option.text}
        </MenuItem>
    ));

    const directions = createOptions(sugoSessionDirections);
    const requestors = createOptions(sugoSessionRequestors);
    const operations = createOptions(sugoSessionOperations);
    const statuses = createOptions(sugoSessionStatuses);

    const changeDateFrom = (date) => {
        dispatch(setFromDateTime(date));
    };

    const changeDateTo = (date) => {
        dispatch(setToDateTime(date));
    };

    const changeDirection = (direction) => {
        dispatch(setDirection(direction));
    };

    const changeRequestor = (requestor) => {
        dispatch(setRequestor(requestor));
    };

    const changeOperation = (operation) => {
        dispatch(setOperation(operation))
    };

    const changeStatus = (status) => {
        dispatch(setStatus(status))
    };

    const searchSessions = () => {
        dispatch(setCurrentPage(0));
        dispatch(requestSessionPage());
    };

    return (
        <Box>
        <Grid container
              spacing={2}
              alignItems={'center'}
              justifyContent={'center'}
        >
            <Grid item>
                <FormControl variant='outlined' size='small' className={classes.formControl}>
                    <TextField
                        label='Z'
                        value={fromDateTime}
                        variant='outlined'
                        placeholder='hh:mm dd/mm/yyyy'
                        onChange={e => changeDateFrom(e.target.value)}
                        size='small'
                        inputProps={{ maxLength: 40 }}
                    />
                </FormControl>
            </Grid>
            <Grid item>
                <FormControl variant='outlined' size='small' className={classes.formControl}>
                    <TextField
                        label='Do'
                        value={toDateTime}
                        variant='outlined'
                        placeholder='hh:mm dd/mm/yyyy'
                        onChange={e => changeDateTo(e.target.value)}
                        size='small'
                        inputProps={{ maxLength: 40 }}
                    />
                </FormControl>
            </Grid>
            <Grid item>
                <SelectorField
                    label='Směr'
                    value={direction}
                    onChange={changeDirection}
                    options={directions}
                    classNames={classes.formControl}
                />
            </Grid>
            <Grid item>
                <SelectorField
                    label='Requestor'
                    value={requestor}
                    onChange={changeRequestor}
                    options={requestors}
                    classNames={classes.formControl}
                />
            </Grid>
            <Grid item>
                <SelectorField
                    label='Operace'
                    value={operation}
                    onChange={changeOperation}
                    options={operations}
                    classNames={classes.formControl}
                />
            </Grid>
            <Grid item>
                <SelectorField
                    label='Status'
                    value={status}
                    onChange={changeStatus}
                    options={statuses}
                    classNames={classes.formControl}
                />
            </Grid>
            <Grid item>
                <SearchButton onClick={searchSessions}>
                    Hledat
                </SearchButton>
            </Grid>
        </Grid>
    </Box>
    );
};

const SelectorField = ({label, value, onChange, options, classNames}) => {
    return (
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
};
