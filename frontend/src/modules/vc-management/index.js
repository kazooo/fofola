import saga from "./saga";
import {vcSlice} from "./slice";

const vcModule = {
    saga,
    reducer: vcSlice.reducer,
};

export default vcModule;
