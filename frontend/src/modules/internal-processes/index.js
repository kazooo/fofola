import saga from "./saga";
import {internalProcessesSlice} from "./slice";

export default {
    saga,
    reducer: internalProcessesSlice.reducer,
}
