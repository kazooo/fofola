import saga from "./saga";
import {homeSlice} from "./slice";

const homeModule = {
    saga,
    reducer: homeSlice.reducer,
};

export default homeModule;
