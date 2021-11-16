import saga from "./saga";
import {solrQuerySlice} from "./slice";

export default {
    saga,
    reducer: solrQuerySlice.reducer,
}
