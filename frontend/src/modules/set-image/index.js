import {setImgSlice} from "./slice";
import saga from "./saga";

const setImgModule = {
    saga,
    reducer: setImgSlice.reducer,
};

export default setImgModule;
