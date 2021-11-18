import {CircularProgress} from "@material-ui/core";
import {VerticalDirectedGrid} from "../layout/VerticalDirectedGrid";

export const LoadingComponent = ({label}) => {
    return <VerticalDirectedGrid>
        <CircularProgress />
        {label}
    </VerticalDirectedGrid>
}
