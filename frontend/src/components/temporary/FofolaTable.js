import {
    Box,
    makeStyles,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@material-ui/core";
import {LoadingComponent} from "./LoadingComponent";

const createStyles = ({width, height}) => {
    const styles = {
        root: {
            backgroundColor: "rgba(255, 255, 255, 1.0)",
            borderRadius: 5,
            border: 1,
            borderColor: 'black',
            borderStyle: 'solid'
        },
        tableContainer: {
            maxHeight: height,
            maxWidth: 1700,
            borderRadius: 5,
        },
        loading: {
            width: width,
            height: 510,
        }
    }
    return makeStyles(styles)();
};

export const FofolaTable = ({columns, rows, height = 600,
                                loadingLabel = null, isLoading = false, paginator = null}) => {

    const classes = createStyles({
        width: columns.reduce((total, arg) => total + arg, 0),
        height: height
    });

    const header = columns.map((column) => (
        <TableCell key={column.id} align={column.align}
            style={{
                minWidth: column.minWidth,
                width: column.width,
                maxWidth: column.maxWidth,
            }}
        >
            {column.label}
        </TableCell>
    ));

    const preparedRows = rows.map((row, index) => (
        <TableRow hover role="checkbox" tabIndex={-1} key={index}>
            {columns.map((column) => {
                const value = row[column.id];
                return (
                    <TableCell key={column.id} align={column.align}>
                        {column.format ? column.format(value) : value}
                    </TableCell>
                );
            })}
        </TableRow>
    ));

    const loading = <TableRow hover role="checkbox" tabIndex={-1}>
        <TableCell colSpan={columns.length} className={classes.loading}>
            <LoadingComponent label={loadingLabel} />
        </TableCell>
    </TableRow>;

    return <Box className={classes.root}>
        <TableContainer className={classes.tableContainer}>
            <Table stickyHeader aria-label="sticky table">
                <TableHead>
                    <TableRow>
                        {header}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {isLoading ? loading : preparedRows}
                </TableBody>
            </Table>
        </TableContainer>
        {paginator}
    </Box>
};
