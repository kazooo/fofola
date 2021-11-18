import {useState} from "react";

export const usePagination = (onChange, defaultPage = 0, defaultItemsPerPage = 10) => {

    const [page, setPage] = useState(defaultPage);
    const [itemsPerPage] = useState(defaultItemsPerPage);

    const nextPage = event => {
        event.preventDefault();
        const newPage = page + 1;
        setPage(newPage);
        onChange(newPage, itemsPerPage);
    }

    const previousPage = event => {
        event.preventDefault();
        if (page < 1) return;
        const newPage = page - 1;
        setPage(newPage);
        onChange(newPage, itemsPerPage);
    }

    const firstPage = event => {
        event.preventDefault();
        setPage(defaultPage);
        onChange(defaultPage, itemsPerPage);
    }

    return [page, nextPage, previousPage, firstPage];
}
