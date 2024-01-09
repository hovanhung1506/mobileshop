function paginate(list) {
    list.forEach((li) => {
        li.addEventListener('click', async () => {
            if(li.classList.contains('active')) {return}
            let search = location.search.substring(1);
            let data = {
                page: li.dataset.page
            }
            if(search) {
                data = JSON.parse('{"' + decodeURI(search)
                    .replace(/"/g, '\\"')
                    .replace(/&/g, '","')
                    .replace(/=/g, '":"') + '"}') || {}
            }
            const host = 'http://localhost:8080' + window.location.pathname
            const loading = document.querySelector('.loading')
            loading.classList.add('active')
            $.ajax({
                type : "POST",
                url : host,
                data : data,
                success : function(data) {
                    const response = data.data
                    const table = document.querySelector('table tbody')
                    // const numberOfElements = document.querySelector('#numberOfElements strong')
                    // numberOfElements.innerHTML = response.numberOfElements
                    table.innerHTML = ''
                    response.content.forEach((product, index) => {
                        table.innerHTML += `
                        <tr>
                            <td>${(response.pageable.pageNumber) * response.pageable.pageSize + index + 1}</td>
                            <td>${product.name}</td>
                            <td data-price="${product.price}">${product.price}</td>
                            <td>${product.quantity}</td>
                            <td>${product.brandName}</td>
                            <td class="text-right">
                                <a href="/admin/product/edit/${product.id}"
                                   class="btn btn-xs btn-flat btn-info">
                                    <i class="fa fa-edit"></i>
                                </a>
                                <a href="/admin/product/delete/${product.id}"
                                   class="btn btn-xs btn-flat btn-danger">
                                    <i class="fa fa-remove"></i>
                                </a>
                            </td>
                        </tr>`
                    })
                    currencyVND()
                    loading.classList.remove('active')
                },
                error : function(e) {
                    console.log("ERROR: ", e);
                }
            });

        })
    })
}