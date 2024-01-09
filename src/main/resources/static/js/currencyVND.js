function currencyVND () {
    const priceEls = document.querySelectorAll('[data-price]')
    priceEls.forEach((priceEl) => {
        let price = parseInt(priceEl.dataset.price)
        priceEl.innerHTML = price.toLocaleString('vn-VN', {
            style: 'currency',
            currency: 'VND'
        })
    })
}