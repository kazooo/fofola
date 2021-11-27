import {useDispatch, useSelector} from "react-redux";
import {getUuidInfo} from "./slice";
import {Box} from "@material-ui/core";
import {FofolaTable} from "../../components/table/FofolaTable";
import {columns} from "./constants";
import {privateUuids, publicUuids, reindexUuids} from "./saga";
import {LockIconButton, RefreshIconButton, UnlockIconButton} from "../../components/button/iconbuttons";

export const UuidInfoPanel = () => {

    const dispatch = useDispatch();
    const uuidInfo = useSelector(state => getUuidInfo(state));

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            ...row,
            action:
                <Box>
                    <RefreshIconButton
                        onClick={() => dispatch(reindexUuids([row.uuid]))}
                        tooltip={"Reindexovat"}
                    />
                    <UnlockIconButton
                        onClick={() => dispatch(publicUuids([row.uuid]))}
                        tooltip={"Zveřejnit"}
                    />
                    <LockIconButton
                        onClick={() => dispatch(privateUuids([row.uuid]))}
                        tooltip={"Zneveřejnit"}
                    />
                </Box>
        }));
    }

    const preparedRows = createDataWithButtons(uuidInfo);

    return uuidInfo.length > 0 && <Box>
        <FofolaTable columns={columns} rows={preparedRows} />
    </Box>
};
