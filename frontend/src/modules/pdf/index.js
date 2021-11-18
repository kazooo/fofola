import saga from "./saga";
import {pdfSlice} from "./slice";

const pdfModule = {
    saga,
    reducer: pdfSlice.reducer,
}

export default pdfModule;
