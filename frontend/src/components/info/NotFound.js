import SearchOutlined from '@material-ui/icons/SearchOutlined';
import {VerticalDirectedGrid} from "../layout/VerticalDirectedGrid";

export const NotFound = ({label}) => {
    return <VerticalDirectedGrid>
        <SearchOutlined fontSize='large' color='error' />
        {label}
    </VerticalDirectedGrid>
};
