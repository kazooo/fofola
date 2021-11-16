import saga from "./saga";
import {linkDonatorSlice} from "./slice";

export default {
    saga,
    reducer: linkDonatorSlice.reducer,
}
