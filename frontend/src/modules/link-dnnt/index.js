import {linkDnntSlice} from "./slice";
import saga from "./saga";

export default {
    saga,
    reducer: linkDnntSlice.reducer,
};