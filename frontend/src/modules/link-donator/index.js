import saga from "./saga";
import {linkDonatorSlice} from "./slice";

const linkDonatorModule = {
    saga,
    reducer: linkDonatorSlice.reducer,
}

export default linkDonatorModule;
