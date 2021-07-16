import {usePagination} from "../../effects/usePagination";
import {HorizontalDirectedGrid} from "./HorizontalDirectedGrid";
import {FirstIconButton, NextIconButton, PreviousIconButton} from "../button/iconbuttons";

export const Paginator = ({defaultPage, onChange}) => {

    const [page, nextPage, previousPage, firstPage] = usePagination(onChange, defaultPage);

    return <HorizontalDirectedGrid>
        {page !== 0 &&
            <FirstIconButton onClick={firstPage} />
        }
        {page !== 0 &&
            <PreviousIconButton onClick={previousPage} />
        }
        <span>{page}</span>
        <NextIconButton onClick={nextPage} />
    </HorizontalDirectedGrid>
}
