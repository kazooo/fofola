import {linkDnntSlice} from "./slice";
import saga from "./saga";

const linkDnntModule = {
    saga,
    reducer: linkDnntSlice.reducer,
};

export default linkDnntModule;
