import {useDispatch, useSelector} from "react-redux";
import {getOutputFiles} from "./slice";
import {Box} from "@material-ui/core";
import {downloadOutputFile, removeOutputFile} from "./saga";
import {FofolaTable} from "../../components/table/FofolaTable";
import {columns} from "./constants";
import {DeleteIconButton, DownloadIconButton} from "../../components/button/iconbuttons";

export const SolrQueryFileTable = () => {

    const dispatch = useDispatch();
    const outputFiles = useSelector(state => getOutputFiles(state));

    const readyToDownload = fileName => fileName && fileName !== '' &&
        !fileName.includes('terminated') && !fileName.includes('not-ready');

    const createDataWithButtons = (data) => (
        data.map((fileName) => ({
                filename: fileName,
                action:
                    <Box>
                        {
                            readyToDownload(fileName) &&
                            <DownloadIconButton onClick={() => dispatch(downloadOutputFile(fileName))} />
                        }
                        <DeleteIconButton onClick={() => dispatch(removeOutputFile(fileName))} />
                    </Box>
            })
        )
    )

    const preparedRows = createDataWithButtons(outputFiles);

    return outputFiles.length > 0 && <Box>
        <FofolaTable columns={columns} rows={preparedRows} />
    </Box>
};
