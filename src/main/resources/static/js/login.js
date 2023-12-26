const loginForm = document.querySelector('#loginForm')
const usernameEl = document.querySelector('#username')
const passwordEl = document.querySelector('#password')

loginForm.addEventListener('submit',  function(e){
    e.preventDefault();
    $.ajax({
        type : "POST",
        url : "http://localhost:8080/login",
        data : $('#loginForm').serialize(),
        success : function(data) {
            if(data.status === 'Failed') {
                loginForm.querySelector('.error-message').innerHTML = data.message
            }
            if(data.status === 'OK') {
                loginForm.submit();
            }
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
})

usernameEl.addEventListener('input', function (e) {
    this.value = this.value.trim();
    loginForm.querySelector('.error-message').innerHTML= ''
})

passwordEl.addEventListener('input', function (e){
    this.value = this.value.trim();
    loginForm.querySelector('.error-message').innerHTML = ''
})