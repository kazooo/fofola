import {useDispatch, useSelector} from "react-redux";
import {getOutputFiles} from "./slice";
import {Box, makeStyles} from "@material-ui/core";
import {DeleteIconButton, DownloadIconButton} from "../../components/button/iconbuttons";
import {FofolaTable} from "../../components/table/FofolaTable";
import {downloadPdfFile, removePdfFile} from "./saga";
import {columns} from "./constants";

const useStyles = makeStyles({
    container: {
        width: 600,
        height: 400,
    }
});

export const PdfFileList = () => {

    const classes = useStyles();
    const dispatch = useDispatch();
    const outputFiles = useSelector(state => getOutputFiles(state));

    const readyToDownload = state => state !== 'EXCEPTION' && state !== 'ACTIVE' && state !== 'WAITING';

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            ...row,
            action:
                <Box>
                    {
                        readyToDownload(row.state) &&
                        <DownloadIconButton
                            onClick={() => dispatch(downloadPdfFile(row.uuid))}
                            tooltip={"Stahnout soubor"}
                        />
                    }
                    <DeleteIconButton
                        onClick={() => dispatch(removePdfFile(row.id))}
                        tooltip={"Vymazat soubor"}
                    />
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(outputFiles);

    return outputFiles.length > 0 && <Box className={classes.container}>
        <FofolaTable height={400} columns={columns} rows={preparedRows} />
    </Box>
};
