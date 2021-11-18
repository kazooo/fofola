import saga from "./saga";
import {uuidInfoSlice} from "./slice";

const uuidInfoModule = {
    saga,
    reducer: uuidInfoSlice.reducer,
}

export default uuidInfoModule;
