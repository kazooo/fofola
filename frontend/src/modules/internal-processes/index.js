import saga from "./saga";
import {internalProcessesSlice} from "./slice";

const internalProcessesModule = {
    saga,
    reducer: internalProcessesSlice.reducer,
}

export default internalProcessesModule;
