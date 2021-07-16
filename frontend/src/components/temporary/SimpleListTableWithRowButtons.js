import {Paper, Table, TableBody, TableCell, TableContainer, TableRow} from "@material-ui/core";

export const SimpleListTableWithRowButtons = ({rows}) => {
    return <TableContainer component={Paper} style={{ width: 600, maxHeight: 700 }}>
        <Table aria-label="simple table">
            <TableBody>
                {rows.map((row, index) => (
                    <TableRow key={index}>
                        <TableCell align="center">{row.data}</TableCell>
                        <TableCell align="center">{row.buttons}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    </TableContainer>
}
