const ulTag = document.querySelector('.pagination');

function pagination2(totalPages, page) {
    let liTag = '';
    let beforePages = page - 1;
    let afterPages = page + 1;
    let activeLi;

    let uri = window.location.pathname;
    let search = location.search.substring(1);
    if (search) {
        let params = JSON.parse('{"' + decodeURI(search)
            .replace(/"/g, '\\"')
            .replace(/&/g, '","')
            .replace(/=/g, '":"') + '"}') || {}
        params = {
            ...params,
            page: page
        }
        if (params?.year) {
            let {year, ...newPrams} = params
            params = newPrams
        }
        uri += '?' + $.param(params)
        uri = uri.replace("+", "%20")
        history.pushState(null, null, uri)
        delete params.page;
        if(JSON.stringify(params) === "{}") {
            uri = window.location.pathname + '?page='
        }else
        uri = window.location.pathname + '?' + $.param(params) + '&page='
    } else {
        uri += '?page='
        uri = uri.replace("+", "%20")
        history.pushState(null, null, uri + page)
    }


    if (page > 1) {
        liTag += `<li onclick="pagination2(${totalPages}, ${page - 1})" data-page="${page - 1}">
                        <a 	data-href="${uri + (page - 1)}"
                            style="display: flex; align-items: center"
                            href="javascript:void(0)"
                        >
                            <i class="fa fa-caret-left" aria-hidden="true" style="font-size:20px"></i>
                        </a>
				  </li>`;
    }
    if (page > 2) {
        liTag += `<li onclick="pagination2(${totalPages}, 1)" data-page="${1}">
						  <a data-href="${uri + 1}"
						     href="javascript:void(0)"
						  >1</a>
				  </li>`;
        if (page > 3) {
            liTag += `<li class="dots"><span>...</span></li>`;
        }
    }

    if (totalPages > 5) {
        if (page === totalPages) {
            beforePages = beforePages - 2;
        } else if (page === totalPages - 1) {
            beforePages = beforePages - 1;
        }
        if (page === 1) {
            afterPages = afterPages + 2;
        } else if (page === 2) {
            afterPages = afterPages + 1;
        }
    }

    for (let index = beforePages; index <= afterPages; index++) {
        if (index > totalPages) {
            continue;
        }
        if (index === 0) {
            index++;
        }
        if (page === index) {
            activeLi = 'active';
        } else {
            activeLi = '';
        }
        liTag += `<li class="${activeLi}" onclick="pagination2(${totalPages}, ${index})" data-page="${index}">
				  		<a data-href="${uri + index}" href="javascript:void(0)">${index}</a>
				  </li>`;
    }

    if (page < totalPages - 1) {
        if (page < totalPages - 2) {
            liTag += `<li class="dots"><span>...</span></li>`;
        }
        liTag += `<li onclick="pagination2(${totalPages}, ${totalPages})" data-page="${totalPages}">
			 	  		<a data-href="${uri + totalPages}"
			 	  		   href="javascript:void(0)"
			 	  		>${totalPages}</a>
				  </li>`;
    }

    if (page < totalPages) {
        liTag += `<li onclick="pagination2(${totalPages}, ${page + 1})" data-page="${page + 1}">
                        <a data-href="${uri + (page + 1)}" 
                           href="javascript:void(0)" 
                           style="display: flex; align-items: center">
                            <i class="fa fa-caret-right" aria-hidden="true" style="font-size:20px"></i>
                        </a>
				 </li>`;
    }
    ulTag.innerHTML = liTag;
    paginate(document.querySelectorAll('.pagination li'))
}

