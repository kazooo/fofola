import {useTranslation} from 'react-i18next';
import {Box, makeStyles, Table, TableCell, TableContainer, TableRow} from '@material-ui/core';
import {Loading} from 'components/info/Loading';
import {NotFound} from 'components/info/NotFound';
import {Error} from 'components/info/Error';

export const VerticalTable = ({
    columns,
    rows,
    loadingLabel = 'common.table.loading.active',
    notFoundLabel = 'common.table.loading.notFound',
    errorLabel = 'common.table.loading.error',
    isLoading = false,
    failed = false,
}) => {
    const {t} = useTranslation();
    const classes = createStyle();

    const preparedRows = columns.map((column) => (
        <TableRow>
            <TableCell
                variant={'head'}
                key={column.id}
                align={column.header.align}
                style={column.header}
            >
                {t(column.label)}
            </TableCell>
            {rows.map((row, index) => {
                const value = row[column.id];
                const formattedValue = column.format ? column.format(value) : value;
                return (
                    <TableCell
                        key={`${column.id}-${index}`}
                        align={column.value.align}
                        style={column.value}
                    >
                        {formattedValue}
                    </TableCell>
                );
            })}
        </TableRow>
    ));

    const loading = (
        <Box className={classes.noRows}>
            <Loading label={loadingLabel} />
        </Box>
    );

    const noRows = (
        <Box className={classes.noRows}>
            <NotFound label={notFoundLabel} />
        </Box>
    );

    const error = (
        <Box className={classes.noRows}>
            <Error label={errorLabel} />
        </Box>
    );

    const table = (
        <TableContainer>
            <Table stickyHeader aria-label='sticky table'>
                {preparedRows}
            </Table>
        </TableContainer>
    );

    return (
        <Box className={classes.root}>
            {failed ? error : <></>}
            {isLoading ? loading : <></>}
            {!failed && rows.length === 0 ? noRows : <></>}
            {!failed && rows.length > 0 ? table : <></>}
        </Box>
    );
};

const createStyle = makeStyles((theme) => ({
    root: {
        height: '100%',
        width: '100%',
    },
    noRows: {
        height: '100%',
        width: '100%',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
    }
}));
