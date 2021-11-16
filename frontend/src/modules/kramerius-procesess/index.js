import saga from "./saga";
import {krameriusProcessSlice} from "./slice";

export default {
    saga,
    reducer: krameriusProcessSlice.reducer,
}
