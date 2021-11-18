import {useSelector} from "react-redux";

import {SimpleListTable} from "../../components/table/SimpleListTable";
import {getUuids} from "./slice";

export const PdfUuidList = () => {

    const uuids = useSelector(state => getUuids(state));

    return uuids.length > 0 && <SimpleListTable maxHeight={300} rows={uuids}/>;
};
