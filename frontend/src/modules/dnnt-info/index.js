import {dnntInfoSlice} from "./slice";
import saga from './saga';

const dnntInfoModule = {
    saga,
    reducer: dnntInfoSlice.reducer,
};

export default dnntInfoModule;
