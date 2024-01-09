const oldPasswordInput = document.querySelector('#old-password');
const newPasswordInput = document.querySelector('#new-password');
const rePasswordInput = document.querySelector('#re-password')
const form = document.querySelector('#form');
const loading = document.querySelector('.loading');

form.addEventListener('submit', function(e) {
    e.preventDefault();

    let flag = false;

    if(oldPasswordInput.value === "") {
        oldPasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu cũ không được để trống';
        oldPasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        oldPasswordInput.focus();
        flag = true;
    }else {
        oldPasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = '';
        oldPasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.remove('error')
    }

    if(newPasswordInput.value === "") {
        newPasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu mới không được để trống';
        newPasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }else {
        newPasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = '';
        newPasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.remove('error')
    }

    if(rePasswordInput.value === "") {
        rePasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu nhập lại không được để trống';
        rePasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }

    if(newPasswordInput.value !== ""&& rePasswordInput.value !== "" && newPasswordInput.value !== rePasswordInput.value) {
        rePasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu nhập lại không đúng';
        rePasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }

    if(!flag) {
        loading.classList.add('active')
        setTimeout(() => {
            form.submit();
        }, 1000)
    }
})