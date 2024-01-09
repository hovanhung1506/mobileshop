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
                    response.content.forEach((order, index) => {
                        table.innerHTML += `
                        <tr>
                            <td>${order.id}</td>
                            <td>${order.customer.name}</td>
                            <td>${order.customer.address}</td>
                            <td>${order.customer.phone}</td>
                            <td data-price="${order.total}">${order.total}</td>
                            <td>${order.status}</td>
                            <td>
                                <div>
                                    <a href="/admin/order/edit/${order.id}"
                                       class="btn btn-xs btn-flat btn-info">
                                        <i class="fa fa-eye" aria-hidden="true"></i>
                                    </a>
                                </div>
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