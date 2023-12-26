const oldPasswordInput = document.querySelector('#old-password');
const newPasswordInput = document.querySelector('#new-password');
const rePasswordInput = document.querySelector('#re-password')
const form = document.querySelector('#form');

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
    }

    if(newPasswordInput.value === "") {
        newPasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu mới không được để trống';
        newPasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }

    if(rePasswordInput.value === "") {
        rePasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu nhập lại không được để trống';
        rePasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }

    if(newPasswordInput.value !== rePasswordInput.value) {
        rePasswordInput.closest('.item')
            .querySelector('.error-message').innerHTML = 'Mật khẩu nhập lại không đúng';
        rePasswordInput.closest('.item')
            .querySelector('.W50dp5').classList.add('error')
        flag = true;
    }

    if(!flag) {
        form.submit();
    }
})