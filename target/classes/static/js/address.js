$(document).ready(function () {
    const provinceEl = document.getElementById('province');
    const districtEl = document.getElementById('district');
    const wardEl = document.getElementById('ward');
    const host = 'https://vnprovinces.pythonanywhere.com/api';

    const fetchAPI = async (url) => {
        const loading = document.querySelector('.loading');
        loading && loading.classList.add('active')
        const response = await fetch(url);
        loading && loading.classList.remove('active')
        return await response.json();
    };

    const changeProvince = async () => {
        districtEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Quận / Huyện</option>';
        wardEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Xã</option>';
        const code = provinceEl.value;
        if (code === '') return;
        const url = `${host}/provinces/${code}`;
        const { districts } = await fetchAPI(url);
        districts.map((item) => {
            const option = `<option value="${item.id}">${item.full_name}</option>`;
            districtEl.innerHTML += option;
        });
    };

    const changeDistrict = async () => {
        wardEl.innerHTML = '<option value="" disabled="disabled" selected>Chọn Xã</option>';
        const code = districtEl.value;
        if (code === '') return;
        const url = `${host}/districts/${code}`;
        const { wards } = await fetchAPI(url);
        wards.map((item) => {
            const option = `<option value=${item.id}>${item.full_name}</option>`;
            wardEl.innerHTML += option;
        });
    };

    const changeWard = () => {
        const provinceName = provinceEl.options[provinceEl.selectedIndex].text;
        const districtName = districtEl.options[districtEl.selectedIndex].text;
        const wardName = wardEl.options[wardEl.selectedIndex].text;
        const address = `${provinceName}, ${districtName}, ${wardName}`;
        console.log(address);
    };

    (async () => {
        const url = host + '/provinces/?basic=true&limit=100';
        const data = await fetchAPI(url);
        data.results.map((item) => {
            const option = `<option value=${item.id}>${item.full_name}</option>`;
            provinceEl.innerHTML += option;
        });
        const province = $('#province')
        const district = $('#district')
        const ward = $('#ward')
        province.select2();
        district.select2();
        ward.select2();

        province.change(changeProvince);
        district.change(changeDistrict);
        ward.change(changeWard);
    })();
});
