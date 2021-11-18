import {makeStyles, Paper, Table, TableBody, TableCell, TableContainer, TableRow} from "@material-ui/core";

const createStyles = ({maxHeight}) => {
    const styles = {
        container: {
            width: 600,
            maxHeight: maxHeight,
        }
    }
    return makeStyles(styles)();
};

export const SimpleListTable = ({rows, maxHeight = 700}) => {

    const classes = createStyles({maxHeight: maxHeight});

    return <TableContainer component={Paper} className={classes.container}>
        <Table aria-label="simple table">
            <TableBody>
                {rows.map((row, index) => (
                    <TableRow key={index}>
                        <TableCell align="center">{row}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    </TableContainer>
};
