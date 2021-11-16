import saga from "./saga";
import {reindexSlice} from "./slice";

export default {
    saga,
    reducer: reindexSlice.reducer,
}
