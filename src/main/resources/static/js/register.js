const formRegister = document.querySelector('#formRegister')
const eyes = document.querySelectorAll('.input-icon')

formRegister.addEventListener('submit', async function (e) {
    e.preventDefault();
    const fullNameEl = this.querySelector('input[name="fullName"]')
    const usernameEl = this.querySelector('input[name="username"]')
    const passwordEl = this.querySelector('input[name="password"]')
    const rePasswordEl = this.querySelector('input[name="re-password"]')
    const emailEl = this.querySelector('input[name="email"]')
    const phoneEl = this.querySelector('input[name="phone"]')
    const selProvinceEl = this.querySelector('select[name="province"]')
    const selDistrictEl = this.querySelector('select[name="district"]')
    const selWardEl = this.querySelector('select[name="ward"]')
    const provinceEl = selProvinceEl.options[selProvinceEl.selectedIndex]
    const districtEl = selDistrictEl.options[selDistrictEl.selectedIndex]
    const wardEl = selWardEl.options[selWardEl.selectedIndex]
    const specificAddressEl = this.querySelector('input[name="specific-address"]')

    let flagError = false

    if (!fullNameEl.value.trim()) {
        const pMessageEl = fullNameEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Họ tên là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = fullNameEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (!usernameEl.value.trim()) {
        const pMessageEl = usernameEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Tên đăng nhập là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = usernameEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (!passwordEl.value.trim()) {
        const pMessageEl = passwordEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Mật khẩu là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = passwordEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (!rePasswordEl.value.trim()) {
        const pMessageEl = rePasswordEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Mật khẩu nhập lại là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = rePasswordEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }

    if (passwordEl.value.trim() !== '' && rePasswordEl.value.trim() !== ''
        && passwordEl.value.trim() !== rePasswordEl.value.trim()) {
        const pMessageEl = rePasswordEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Mật khẩu nhập lại không đúng"
        flagError = true
    }

    if (!emailEl.value.trim()) {
        const pMessageEl = emailEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Email là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = emailEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (!phoneEl.value.trim()) {
        const pMessageEl = phoneEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Số điện thoại là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = phoneEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (provinceEl.hasAttribute('disabled')) {
        const pMessageEl = selProvinceEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Tỉnh là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = selProvinceEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (districtEl.hasAttribute('disabled')) {
        const pMessageEl = selDistrictEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Quận / Huyện là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = selDistrictEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }
    if (wardEl.hasAttribute('disabled')) {
        const pMessageEl = selWardEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = "Phường / Xã là bắt buộc"
        flagError = true
    } else {
        const pMessageEl = selWardEl.closest('.input-group').querySelector('.error-message')
        pMessageEl.innerHTML = ""
    }

    let address
    if (specificAddressEl.value === '') {
        address = `${wardEl.text}, ${districtEl.text}, ${provinceEl.text}`
    } else {
        address = `${specificAddressEl.value.trim()}, ${wardEl.text}, ${districtEl.text}, ${provinceEl.text}`
    }

    if (flagError) {
        return
    }

    const data = {
        name: fullNameEl.value.trim(),
        address: address.trim(),
        phone: phoneEl.value.trim(),
        email: emailEl.value.trim(),
        username: usernameEl.value.trim(),
        password: passwordEl.value.trim(),
    }

    async function register() {
        const host = 'http://localhost:8080/register'
        const response = await fetch(host, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        const responseData = await response.json()
        if (responseData.status === 'Failed') {
            const errors = Object.keys(responseData.errors)
            errors.forEach((error) => {
                const element = document.querySelector(`#${error}`)
                const pMessage = element.closest('.input-group').querySelector('.error-message')
                pMessage.innerHTML = responseData.errors[error]
            })
        }

        if (responseData.status === 'Success') {
            if (confirm('Tạo tài khoản thành công!. Chuyển đến trang đăng nhập')) {
                window.location.href = 'http://localhost:8080/login'
            }
        }
    }

    await register()
})

eyes.forEach((eye) => {
    eye.addEventListener('click', () => {
        const input = eye.closest('.input-group').querySelector('input')
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
})