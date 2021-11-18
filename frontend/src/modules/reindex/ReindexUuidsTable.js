import {SimpleListTable} from "../../components/table/SimpleListTable";
import {useSelector} from "react-redux";
import {getUuids} from "./slice";

export const ReindexUuidsTable = () => {

    const uuids = useSelector(state => getUuids(state));

    return uuids.length > 0 && <SimpleListTable rows={uuids}/>;
};
