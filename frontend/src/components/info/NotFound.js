import SearchOutlined from '@material-ui/icons/SearchOutlined';
import {VerticalDirectedGrid} from "../layout/VerticalDirectedGrid";
import {useTranslation} from "react-i18next";

export const NotFound = ({label}) => {
    const {t} = useTranslation();

    return <VerticalDirectedGrid>
        <SearchOutlined fontSize='large' color='error' />
        {t(label)}
    </VerticalDirectedGrid>
};
