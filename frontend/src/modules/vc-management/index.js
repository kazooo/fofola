import saga from "./saga";
import {vcSlice} from "./slice";

export default {
    saga,
    reducer: vcSlice.reducer,
};
