const ulTag = document.querySelector('.pagination ul');

function pagination(totalPages, page) {
	let liTag = '';
	let beforePages = page - 1;
	let afterPages = page + 1;
	let activeLi;


	let uri = window.location.pathname;
	if(uri.includes('search')) {
		let params = window.location.search;
		uri = uri + params + "&page=";
	}else {
		uri = uri + "?page=";
	}


	if (page > 1) {
		liTag += `<li class="btn prev" onclick="pagination(${totalPages}, ${page - 1})" page="${page - 1}">
						<span>
							<a href="${uri + (page - 1)}"><i class="fa-solid fa-caret-left"></i> Prev</a>
						</span>
				  </li>`;
	}
	if (page > 2) {
		liTag += `<li class="numb" onclick="pagination(${totalPages}, 1)" page="1">
						<span><a href="${uri + 1}">1</a></span>
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
		liTag += `<li class="numb ${activeLi}" onclick="pagination(${totalPages}, ${index})" page="${index}">
				  		<span><a href="${uri + index}">${index}</a></span>
				  </li>`;
	}

	if (page < totalPages - 1) {
		if (page < totalPages - 2) {
			liTag += `<li class="dots"><span>...</span></li>`;
		}
		liTag += `<li class="numb" onclick="pagination(${totalPages}, ${totalPages})" page="${totalPages}">
			 	  		<span><a href="${uri + totalPages}">${totalPages}</a></span>
				  </li>`;
	}

	if (page < totalPages) {
		liTag += `<li class="btn next" onclick="pagination(${totalPages}, ${page + 1})" page="${page + 1}">
						<span>
							<a href="${uri + (page + 1)}">Next <i class="fa-solid fa-caret-right"></i></a>
						</span>
				 </li>`;
	}
	ulTag.innerHTML = liTag;
}
