import {useDispatch, useSelector} from "react-redux";
import {Box} from "@material-ui/core";

import {getOutputFiles} from "./slice";
import {downloadOutputFile, removeFile} from "./saga";
import {FofolaTable} from "../../components/table/FofolaTable";
import {DeleteIconButton, DownloadIconButton} from "../../components/button/iconbuttons";
import {columns} from "./constants";

export const CheckDonatorFileTable = () => {

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
                            <DownloadIconButton
                                onClick={() => dispatch(downloadOutputFile(fileName))}
                                tooltip={"Stahnout soubor"}
                            />
                        }
                        <DeleteIconButton
                            onClick={() => dispatch(removeFile(fileName))}
                            tooltip={"Vymazat soubor"}
                        />
                    </Box>
            })
        )
    )

    const preparedRows = createDataWithButtons(outputFiles);

    return outputFiles.length > 0 && <Box>
        <FofolaTable columns={columns} rows={preparedRows} />
    </Box>
}
