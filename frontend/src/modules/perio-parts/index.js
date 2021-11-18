import saga from "./saga";
import {perioPartsPublishSlice} from "./slice";

const perioPartsPublishModule = {
    saga,
    reducer: perioPartsPublishSlice.reducer,
}

export default perioPartsPublishModule;
