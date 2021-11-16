import {Paginator} from "../../components/form/Paginator";
import {getCurrentPage, setPage} from "./slice";
import {requestProcessesInfo} from "./saga";
import {useDispatch, useSelector} from "react-redux";

export const ProcessPaginator = () => {

    const dispatch = useDispatch();
    const currentPage = useSelector(state => getCurrentPage(state));

    const previousPage = () => {
        if (currentPage > 0) {
            dispatch(setPage(currentPage - 1));
            dispatch(requestProcessesInfo());
        }
    }

    const nextPage = () => {
        dispatch(setPage(currentPage + 1));
        dispatch(requestProcessesInfo());
    }

    return <Paginator
        currentPage={currentPage}
        previousFunc={previousPage}
        nextFunc={nextPage}
    />
};
