import {useDispatch} from "react-redux";
import {DownloadButton, RemoveButton} from "../../components/button";
import {downloadOutputFile, removeFile} from "./saga";

export const FileRow = ({fileName}) => {

    const dispatch = useDispatch();

    return <tr>
        <td>{fileName}</td>
        <td>
            <RemoveButton onClick={() => dispatch(removeFile(fileName))} />
            <DownloadButton onClick={() => dispatch(downloadOutputFile(fileName))} />
        </td>
    </tr>
};
