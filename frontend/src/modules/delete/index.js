import saga from "./saga";
import {deleteSlice} from "./slice";

export default {
    saga,
    reducer: deleteSlice.reducer,
}
