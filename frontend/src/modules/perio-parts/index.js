import saga from "./saga";
import {perioPartsPublishSlice} from "./slice";

export default {
    saga,
    reducer: perioPartsPublishSlice.reducer,
}
