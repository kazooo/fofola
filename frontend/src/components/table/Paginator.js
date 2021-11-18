import {FirstIconButton, NextIconButton, PreviousIconButton} from "../button/iconbuttons";
import {HorizontalDirectedGrid} from "../layout/HorizontalDirectedGrid";
import {usePagination} from "../../effects/usePagination";

export const Paginator = ({defaultPage, onChange, enabled = true}) => {

    const [page, nextPage, previousPage, firstPage] = usePagination(onChange, defaultPage);

    return enabled && <HorizontalDirectedGrid>
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
