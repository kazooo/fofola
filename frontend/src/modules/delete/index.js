import saga from "./saga";
import {deleteSlice} from "./slice";

const deleteModule = {
    saga,
    reducer: deleteSlice.reducer,
}

export default deleteModule;
