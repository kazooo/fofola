import {useDispatch} from "react-redux";
import {LoadUuidsForm} from "../../components/temporary/LoadUuidsForm";
import {addUuids} from "./slice";

export const DeleteForm = () => {

    const dispatch = useDispatch();

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    }

    return <LoadUuidsForm addUuids={loadUuids}/>;
}
