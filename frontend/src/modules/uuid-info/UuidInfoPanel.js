import {useDispatch, useSelector} from "react-redux";
import {Box} from "@material-ui/core";

import {
    CloseIconButton,
    LockIconButton,
    RefreshIconButton,
    UnlockIconButton
} from "../../components/button/iconbuttons";
import {FofolaTable} from "../../components/table/FofolaTable";
import {getOneUuidInfo, privateUuids, publicUuids, reindexUuids} from "./saga";
import {getUuidInfo, removeUuidInfo} from "./slice";
import {columns} from "./columns";

export const UuidInfoPanel = () => {

    const dispatch = useDispatch();
    const uuidInfo = useSelector(state => getUuidInfo(state));

    const createDataWithButtons = (data) => {
        return data.map((row) => ({
            tableAction: (
                <Box>
                    <RefreshIconButton
                        onClick={() => dispatch(getOneUuidInfo(row.uuid))}
                        tooltip={"Obnovit"}
                    />
                    <CloseIconButton
                        onClick={() => dispatch(removeUuidInfo(row.uuid))}
                        tooltip={"Vymazat z tabulky"}
                    />
                </Box>
            ),
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
