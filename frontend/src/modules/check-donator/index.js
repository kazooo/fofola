import saga from "./saga";
import {checkDonatorSlice} from "./slice";

export default {
    saga,
    reducer: checkDonatorSlice.reducer,
}
