export const Paginator = ({currentPage, nextFunc, previousFunc}) => {
    return <div>
        <span onClick={previousFunc}>Previous</span>
        <span>{currentPage}</span>
        <span onClick={nextFunc}>Next</span>
    </div>
};
