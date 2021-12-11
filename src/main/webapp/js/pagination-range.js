function findPaginationRange(currentPage, pagesCount) {
    let delta = 2,
        range = [],
        rangeWithDots = [],
        l;

    range.push(1)
    for (let i = currentPage - delta; i <= currentPage + delta; i++) {
        if (i < pagesCount && i > 1) {
            range.push(i);
        }
    }
    if (pagesCount != 1){
        range.push(pagesCount);
    }

    for (let i of range) {
        if (l) {
            if (i - l === 2) {
                rangeWithDots.push(l + 1);
            } else if (i - l !== 1) {
                rangeWithDots.push('...');
            }
        }
        rangeWithDots.push(i);
        l = i;
    }

    return rangeWithDots;
}