const loginForm = document.querySelector('#loginForm')
const usernameEl = document.querySelector('#username')
const passwordEl = document.querySelector('#password')
const eye = document.querySelector('.input-icon')
const loading = document.querySelector('.loading')

loginForm.addEventListener('submit', function (e) {
    e.preventDefault();
    loading.classList.add('active')
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/login",
        data: $('#loginForm').serialize(),
        success: function (data) {
            if (data.status === 'Failed') {
                loading.classList.remove('active')
                loginForm.querySelector('.error-message').innerHTML = data.message
            }
            if (data.status === 'OK') {
                loginForm.submit();
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
})

usernameEl.addEventListener('input', function (e) {
    this.value = this.value.trim();
    loginForm.querySelector('.error-message').innerHTML = ''
})

passwordEl.addEventListener('input', function (e) {
    this.value = this.value.trim();
    loginForm.querySelector('.error-message').innerHTML = ''
})

eye.addEventListener('click', () => {
    const input = eye.closest('.input-group').querySelector('input');
    const type = input.getAttribute('type');
    if (type === 'password') {
        eye.classList.remove('zmdi-eye-off')
        eye.classList.add('zmdi-eye')
        input.setAttribute('type', 'text')
    } else {
        eye.classList.remove('zmdi-eye')
        eye.classList.add('zmdi-eye-off')
        input.setAttribute('type', 'password')
    }
})