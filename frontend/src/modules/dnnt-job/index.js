import {dnntJobSlice} from './slice';
import saga from './saga';

const dnntJobsModule = {
    saga,
    reducer: dnntJobSlice.reducer,
};

export default dnntJobsModule;
