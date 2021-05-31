import {useSelector} from "react-redux";
import {Table} from "../../components/container/Table";
import {getOutputFiles} from "./slice";
import {FileRow} from "./FileRow";

export const FileTable = () => {

    const outputFiles = useSelector(state => getOutputFiles(state));

    return outputFiles.length > 0 &&
        <div>
            <Table>
                {outputFiles.map(file => <FileRow fileName={file} />)}
            </Table>
        </div>
};
