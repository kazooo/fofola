import saga from "./saga";
import {solrQuerySlice} from "./slice";

const solrQueryModule = {
    saga,
    reducer: solrQuerySlice.reducer,
}

export default solrQueryModule;

