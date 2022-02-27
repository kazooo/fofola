import {dnntSessionSlice} from './slice';
import saga from './saga';

const dnntSessionModule = {
    saga,
    reducer: dnntSessionSlice.reducer,
};

export default dnntSessionModule;
