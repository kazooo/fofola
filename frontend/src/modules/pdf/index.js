import saga from "./saga";
import {pdfSlice} from "./slice";

export default {
    saga,
    reducer: pdfSlice.reducer,
}
