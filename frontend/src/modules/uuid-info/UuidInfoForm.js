import {useDispatch, useSelector} from "react-redux";
import {LoadUuidsForm} from "components/form/LoadUuidsForm";
import {clearUuidInfo, getUuidInfo} from "./slice";
import {getUuidInfo as getUuidInfoRequest} from "./saga";
import {Box} from "@material-ui/core";
import {ClearButton} from "components/button";
import {VerticalDirectedGrid} from "components/layout/VerticalDirectedGrid";

export const UuidInfoForm = () => {

    const dispatch = useDispatch();
    const infos = useSelector(state => getUuidInfo(state));

    const sendUuids = uuids => {
        dispatch(getUuidInfoRequest(uuids));
    };

    const clear = () => {
        dispatch(clearUuidInfo());
    };

    return <Box>
        <VerticalDirectedGrid>
            <LoadUuidsForm addUuids={sendUuids}/>
            {infos.length > 0 &&
                <Box style={{ paddingTop: '30px'}}>
                    <ClearButton onClick={clear}>Vyčistit</ClearButton>
                </Box>
            }
        </VerticalDirectedGrid>
    </Box>
};
