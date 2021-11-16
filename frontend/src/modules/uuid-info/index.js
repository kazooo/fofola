import saga from "./saga";
import {uuidInfoSlice} from "./slice";

export default {
    saga,
    reducer: uuidInfoSlice.reducer,
}
