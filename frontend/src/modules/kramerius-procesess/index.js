import saga from "./saga";
import {krameriusProcessSlice} from "./slice";

const krameriusProcessesModule = {
    saga,
    reducer: krameriusProcessSlice.reducer,
}

export default krameriusProcessesModule;
