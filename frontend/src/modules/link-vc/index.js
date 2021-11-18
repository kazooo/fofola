import saga from "./saga";
import {linkVcSlice} from "./slice";

const linkVcModule = {
    saga,
    reducer: linkVcSlice.reducer,
}

export default linkVcModule;
