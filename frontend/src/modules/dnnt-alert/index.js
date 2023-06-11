import {dnntAlertSlice} from './slice';
import saga from './saga';

const dnntAlertModule = {
    saga,
    reducer: dnntAlertSlice.reducer,
};

export default dnntAlertModule;
