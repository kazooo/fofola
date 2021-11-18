import saga from "./saga";
import {reindexSlice} from "./slice";

const reindexModule = {
    saga,
    reducer: reindexSlice.reducer,
}

export default reindexModule;
