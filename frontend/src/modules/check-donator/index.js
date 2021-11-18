import saga from "./saga";
import {checkDonatorSlice} from "./slice";

const checkDonatorModule = {
    saga,
    reducer: checkDonatorSlice.reducer,
}

export default checkDonatorModule;
