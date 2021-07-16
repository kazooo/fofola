import {useDispatch} from "react-redux";
import {addUuids} from "./slice";
import {LoadUuidsForm} from "../../components/temporary/LoadUuidsForm";

export const ReindexForm = () => {

    const dispatch = useDispatch();

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    }

    return <LoadUuidsForm addUuids={loadUuids}/>;
};
