const header = document.querySelector('header');
const backToTop = document.querySelector('.backToTop');
const listProductPrice = document.querySelectorAll('.product__list .product__price');


function debounce(fn, ms) {
    let timer;

    return function () {
        const args = arguments;
        const context = this;

        if (timer) clearTimeout(timer);

        timer = setTimeout(() => {
            fn.apply(context, args);
        }, ms);
    };
}


window.addEventListener(
    'scroll',
    debounce(function () {
        if (document.documentElement.scrollTop > 88) {
            header.style.boxShadow = 'rgba(33, 35, 38, 0.1) 0px 10px 10px -10px';
        } else {
            header.style.boxShadow = '';
        }

        if (document.documentElement.scrollTop > 200) {
            backToTop.classList.add('active');
        } else {
            backToTop.classList.remove('active');
        }
    }, 200)
);


listProductPrice.forEach((item) => {
    let price = item.textContent.trim();
    item.innerHTML = currencyFormatter(price);
})

function currencyFormatter(p) {
    p = p + '';
    let s = '', count = 0;
    for (let i = p.length - 1; i >= 0; i--) {
        count++;
        s += p[i];
        if (count === 3 && i !== 0) {
            s += '.';
            count = 0;
        }
    }

    s = s.split('').reverse().join('');
    return s + '<sup>₫</sup>';
}


backToTop.addEventListener('click', function () {
    window.scrollTo({
        top: 0,
        behavior: 'smooth',
    });
});
