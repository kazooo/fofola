import {useSelector} from "react-redux";
import {Table} from "../../components/container/Table";
import {FileRow} from "./FileRow";
import {getOutputFiles} from "./slice";

export const FileTable = () => {

    const outputFiles = useSelector(state => getOutputFiles(state));
    const header = ["Uuid", "Status", "Operace"];

    return outputFiles.length > 0 &&
        <div>
            <Table header={header}>
                {outputFiles.map(info => <FileRow data={info} />)}
            </Table>
        </div>
};