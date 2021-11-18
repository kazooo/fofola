import saga from "./saga";
import {changeAccessSlice} from "./slice";

const changeAccessModule = {
    saga,
    reducer: changeAccessSlice.reducer,
}

export default changeAccessModule;
