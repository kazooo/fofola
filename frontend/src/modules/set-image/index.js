import {setImgSlice} from "./slice";
import saga from "./saga";

export default {
    saga,
    reducer: setImgSlice.reducer,
};
