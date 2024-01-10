const decrease = document.querySelector('.decrease');
const increase = document.querySelector('.increase');
const quantityInput = document.querySelector('#form1 input[name="quantity"]');
const totalPrice = document.querySelector('.total__price');
const price = document.querySelector('.price');
const quantityInput2 = document.querySelector('#form2 input[name="quantity"]')

decrease.addEventListener('click', function () {
    let quantity = parseInt(quantityInput.value);
    if (!decrease.className.includes('disabled')) {
        quantity--;
        quantityInput.value = quantity;
        quantityInput2.value = quantity
        let p = parseInt(totalPrice.getAttribute('price')) * quantity;
        totalPrice.innerHTML = currencyFormatter(p);
    }

    if (quantity <= 1) {
        decrease.classList.add('disabled');
    }
});

increase.addEventListener('click', function () {
    let quantity = parseInt(quantityInput.value);
    quantity++;
    quantityInput.value = quantity;
    quantityInput2.value = quantity
    let p = parseInt(totalPrice.getAttribute('price')) * quantity;
    totalPrice.innerHTML = currencyFormatter(p);
    if (quantity > 1) {
        decrease.classList.remove('disabled');
    }
});

totalPrice.innerHTML = currencyFormatter(totalPrice.textContent.trim())
price.innerHTML = currencyFormatter(price.textContent.trim())

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

quantityInput.addEventListener('input', function () {
    if (!parseInt(this.value) || parseInt(this.value) < 1) {
        this.value = '';
    } else {
        let quantity = parseInt(this.value);
        quantityInput2.value = quantity
        let p = parseInt(totalPrice.getAttribute('price')) * quantity;
        totalPrice.innerHTML = currencyFormatter(p);
        if (quantity > 1) decrease.classList.remove('disabled');
        else decrease.classList.add('disabled');
    }
});

