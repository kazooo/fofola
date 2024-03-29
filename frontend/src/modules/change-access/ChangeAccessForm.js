import {useDispatch} from "react-redux";
import {addUuids} from "./slice";
import {LoadUuidsForm} from "../../components/form/LoadUuidsForm";

export const ChangeAccessForm = () => {

    const dispatch = useDispatch();

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    }

    return <LoadUuidsForm addUuids={loadUuids}/>;
};
