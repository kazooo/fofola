import saga from "./saga";
import {linkVcSlice} from "./slice";

export default {
    saga,
    reducer: linkVcSlice.reducer,
}
