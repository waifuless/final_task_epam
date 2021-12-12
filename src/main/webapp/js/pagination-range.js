function printPagination(page, pagesCount, clickFunctionName, paginationId) {
    const leftArrow = $('#'+paginationId+' li:first');
    const rightArrow = $('#'+paginationId+' li:last');
    if (page == 1) {
        leftArrow.addClass('disabled');
    } else {
        leftArrow.removeClass('disabled');
        leftArrow.attr('onclick', clickFunctionName+'('+(+page-1)+')');
    }
    if (page == pagesCount) {
        rightArrow.addClass('disabled');
    } else {
        rightArrow.removeClass('disabled');
        rightArrow.attr('onclick', clickFunctionName+'('+(+page+1)+')');

    }
    $('#'+paginationId+' li:not(:first):not(:last)').remove();
    let range = findPaginationRange(page, pagesCount);
    for (let elem of range) {
        let classToAdd = '';
        let attrToAdd = '';
        if (Number.isInteger(elem)) {
            if (elem == page) {
                classToAdd = ' active ';
            } else {
                attrToAdd = 'onclick="'+clickFunctionName+'(' + elem + ')"';
            }
        } else {
            classToAdd = ' disabled '
        }
        rightArrow.before(`<li class="page-item `
            + classToAdd + `">
                <button class="page-link" ` + attrToAdd + `>` + elem + `</button>
            </li>`);
    }
}

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
    if (pagesCount > 1){
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