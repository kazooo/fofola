import {useDispatch} from "react-redux";
import {addUuids} from "./slice";
import {LoadUuidsForm} from "../../components/temporary/LoadUuidsForm";
import {useEffect} from "react";
import {useInterval} from "../../effects/useInterval";
import {requestPdfFiles} from "./saga";

export const PdfForm = () => {

    const dispatch = useDispatch();
    const RELOAD_INTERVAL_MS = 5000;

    useEffect(() => {
        dispatch(requestPdfFiles());
    });

    useInterval(() => {
        dispatch(requestPdfFiles());
    }, RELOAD_INTERVAL_MS);

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    }

    return <LoadUuidsForm addUuids={loadUuids}/>;
};
