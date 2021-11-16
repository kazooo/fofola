import saga from "./saga";
import {changeAccessSlice} from "./slice";

export default {
    saga,
    reducer: changeAccessSlice.reducer,
}
