import saga from './saga';
import {dnntTransitionSlice} from "./slice";

const dnntTransitionModule = {
    saga,
    reducer: dnntTransitionSlice.reducer,
};

export default dnntTransitionModule;
