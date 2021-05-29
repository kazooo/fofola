import {useDispatch, useSelector} from "react-redux";
import {getCurrentPage, setPage} from "./slice";
import {requestInternalProcesses} from "./saga";

export const Paginator = () => {

    const dispatch = useDispatch();
    const currentPage = useSelector(state => getCurrentPage(state));

    const previousPage = () => {
        if (currentPage > 0) {
            dispatch(setPage(currentPage - 1));
            dispatch(requestInternalProcesses());
        }
    }

    const nextPage = () => {
        dispatch(setPage(currentPage + 1));
        dispatch(requestInternalProcesses());
    }

    return <Paginator
        currentPage={currentPage}
        previousFunc={previousPage}
        nextFunc={nextPage}
    />
};
