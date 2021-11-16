import {useSelector} from "react-redux";
import {SimpleListTable} from "../../components/temporary/SimpleListTable";
import {getUuids} from "./slice";

export const LinkDnntUuidsTable = () => {
    const uuids = useSelector(state => getUuids(state));

    return uuids.length > 0 && <SimpleListTable rows={uuids}/>;
};
