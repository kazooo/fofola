import {useDispatch} from "react-redux";
import {DownloadButton, RemoveButton} from "../../components/button";
import {downloadPdfFile, removePdfFile} from "./saga";

export const FileRow = ({data}) => {

    const dispatch = useDispatch();

    return <tr>
        <td>{data.uuid}</td>
        <td>{data.state}</td>
        <td>
            <RemoveButton onClick={() => dispatch(removePdfFile(data.uuid))} />
            <DownloadButton onClick={() => dispatch(downloadPdfFile(data.uuid))} />
        </td>
    </tr>
};
