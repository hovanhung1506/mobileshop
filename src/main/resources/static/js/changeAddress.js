const selProvinceEl = document.getElementById('province');
const selDistrictEl = document.getElementById('district');
const selWardEl = document.getElementById('ward');
const host = 'https://vnprovinces.pythonanywhere.com/api';
const form = document.getElementById('form');
const specificAddressEl = document.querySelector('input[name="specific-address"]');

const fetchAPI = async (url) => {
    const loading = document.querySelector('.loading');
    loading && loading.classList.add('active')
    const response = await fetch(url);
    loading && loading.classList.remove('active')
    return await response.json();
};

const changeProvince = async () => {
    selDistrictEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Quận / Huyện</option>';
    selWardEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Xã</option>';
    const code = selProvinceEl.value;
    if (code === '') return;
    const url = `${host}/provinces/${code}`;
    const { districts } = await fetchAPI(url);
    districts.map((item) => {
        const option = `<option value="${item.id}">${item.full_name}</option>`;
        selDistrictEl.innerHTML += option;
    });
};

const changeDistrict = async () => {
    selWardEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Xã</option>';
    const code = selDistrictEl.value;
    if (code === '') return;
    const url = `${host}/districts/${code}`;
    const { wards } = await fetchAPI(url);
    wards.map((item) => {
        const option = `<option value=${item.id}>${item.full_name}</option>`;
        selWardEl.innerHTML += option;
    });
};

const changeWard = () => {
    const provinceName = selProvinceEl.options[selProvinceEl.selectedIndex].text;
    const districtName = selDistrictEl.options[selDistrictEl.selectedIndex].text;
    const wardName = selWardEl.options[selWardEl.selectedIndex].text;
    const address = `${provinceName}, ${districtName}, ${wardName}`;
    // console.log(address);
};

(async () => {
    const url = host + '/provinces/?basic=true&limit=100';
    const data = await fetchAPI(url);
    data.results.map((item) => {
        const option = `<option value=${item.id}>${item.full_name}</option>`;
        selProvinceEl.innerHTML += option;
    });
    const province = $('#province');
    const district = $('#district');
    const ward = $('#ward');
    province.select2();
    district.select2();
    ward.select2();

    province.change(changeProvince);
    district.change(changeDistrict);
    ward.change(changeWard);
})();

form.addEventListener('submit', async function (e) {
    const provinceEl = selProvinceEl.options[selProvinceEl.selectedIndex];
    const districtEl = selDistrictEl.options[selDistrictEl.selectedIndex];
    const wardEl = selWardEl.options[selWardEl.selectedIndex];
    e.preventDefault();
    let flagError = false;
    if (provinceEl.hasAttribute('disabled')) {
        const pMessageEl = selProvinceEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = 'Tỉnh là bắt buộc';
        flagError = true;
    } else {
        const pMessageEl = selProvinceEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = '';
    }
    if (districtEl.hasAttribute('disabled')) {
        const pMessageEl = selDistrictEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = 'Quận / Huyện là bắt buộc';
        flagError = true;
    } else {
        const pMessageEl = selDistrictEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = '';
    }
    if (wardEl.hasAttribute('disabled')) {
        const pMessageEl = selWardEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = 'Phường / Xã là bắt buộc';
        flagError = true;
    } else {
        const pMessageEl = selWardEl.closest('.form-group').querySelector('.error-message');
        pMessageEl.innerHTML = '';
    }

    let address;
    if (specificAddressEl.value === '') {
        address = `${wardEl.text}, ${districtEl.text}, ${provinceEl.text}`;
    } else {
        address = `${specificAddressEl.value.trim()}, ${wardEl.text}, ${districtEl.text}, ${provinceEl.text}`;
    }
    if (flagError) {
        return;
    }

    async function changeAddress() {
        const host = 'http://localhost:8080/user/address'
        const response = await fetch(host, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                address
            })
        })

        const res = await response.json();
        if(res.status === "200") {
            if(confirm(res.message) === true) {
                window.location.href = 'http://localhost:8080/user/profile'
            }
        }
    }
    await changeAddress()
});
