import ErrorOutlineOutlinedIcon from '@material-ui/icons/ErrorOutlineOutlined';
import {VerticalDirectedGrid} from "./VerticalDirectedGrid";

export const Error = ({label}) => {
    return <VerticalDirectedGrid>
        <ErrorOutlineOutlinedIcon fontSize='large' color='error' />
        {label}
    </VerticalDirectedGrid>
}
