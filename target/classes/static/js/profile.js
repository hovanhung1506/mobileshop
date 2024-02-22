const phoneEl = document.querySelector('#phone');
const emailEl = document.querySelector('#email');
const form = document.querySelector('#form');
const loading = document.querySelector('.loading');

function debounce(fn, delay) {
    let timer = null;
    return function () {
        const context = this,
            args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    };
}

emailEl.addEventListener(
    'input',
    debounce(function () {
        const email = emailEl.value;
        if (email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            this.closest('.item').querySelector('.error-message').innerHTML = '';
            this.closest('.item').querySelector('.W50dp5').classList.remove('error');
        } else {
            this.closest('.item').querySelector('.W50dp5').classList.add('error');
            this.closest('.item').querySelector('.error-message').innerHTML = 'Email không hợp lệ';
        }
    }, 500)
);

phoneEl.addEventListener(
    'input',
    debounce(function () {
        const phone = phoneEl.value;
        if (phone.match(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g) && phone.length === 10) {
            console.log('ok')
            this.closest('.item').querySelector('.error-message').innerHTML = '';
            this.closest('.item').querySelector('.W50dp5').classList.remove('error');
        } else {
            console.log('not')
            this.closest('.item').querySelector('.W50dp5').classList.add('error');
            this.closest('.item').querySelector('.error-message').innerHTML = 'Số điện thoại không hợp lệ';
        }
    }, 500)
);

function imageExists(image_url, element) {
    const img = new Image();

    img.onload = function() {};

    img.onerror = function() {
        element.setAttribute('src', '/images/blank-profile.png')
    };

    img.src = image_url;
}

const avatars = document.querySelectorAll('.avatar img')
avatars.forEach((avatar) => {
    imageExists(avatar.getAttribute('src'), avatar)
})

form.addEventListener('submit', (e) => {
    e.preventDefault()
    loading.classList.add('active')
    setTimeout(() => {
        form.submit()
    }, 1000)
})