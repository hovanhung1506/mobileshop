const header = document.querySelector('header');
const backToTop = document.querySelector('.backToTop');

currencyVND()

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



backToTop.addEventListener('click', function () {
    window.scrollTo({
        top: 0,
        behavior: 'smooth',
    });
});
