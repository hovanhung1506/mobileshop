const header = document.querySelector('header');
const buttons = document.querySelectorAll('.ripple');
const priceOrigins = document.querySelectorAll('.price-origin')
const btnDeletes = document.querySelectorAll('.delete')
const quantityItems = document.querySelectorAll('.carts ul li');
const btnDeleteAll = document.querySelector('.delete-all')
const btnOrder = document.querySelector('.ordered')

priceOrigins.forEach((item) => {
    let price = parseInt(item.getAttribute('price-origin'));
    item.innerHTML = currencyFormatter(price)
})

buttons.forEach((btn) => {
    btn.addEventListener('click', function (e) {
        let x = e.clientX - e.target.offsetLeft;
        let y = e.clientY - e.target.offsetTop;
        let ripples = document.createElement('span');
        ripples.style.left = x + 'px';
        ripples.style.top = y + 'px';
        this.appendChild(ripples);

        setTimeout(() => {
            ripples.remove();
        }, 1000);
    });
});

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
    }, 200)
);

function total() {
    const prices = document.querySelectorAll('[price]');
    const totalPriceEle = document.querySelector('.total-price span');
    let totalPrice = 0;
    prices.forEach((item) => {
        const liEle = item.parentElement;
        const price = parseInt(item.getAttribute('price'));
        const quantity = liEle.querySelector('input[name="quantity"]').value;
        totalPrice += price * parseInt(quantity);
        item.innerHTML = currencyFormatter(price * parseInt(quantity));
    });
    totalPriceEle.innerHTML = currencyFormatter(totalPrice);
}

total();

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

quantityItems.forEach((item) => {
    const decrease = item.querySelector('.decrease');
    const increase = item.querySelector('.increase');
    const quantityInput = item.querySelector('input[name="quantity"]');
    const priceEle = item.querySelector('.price-item');

    decrease.addEventListener('click', function () {
        let quantity = parseInt(quantityInput.value);
        if (!decrease.className.includes('disabled')) {
            quantity--;
            quantityInput.value = quantity;
            let p = parseInt(priceEle.getAttribute('price')) * quantity;
            priceEle.innerHTML = currencyFormatter(p);
            total();
            const id = item.querySelector('input[name="id"]').value
            changeQuantity(id, quantity)
        }

        if (quantity <= 1) {
            decrease.classList.add('disabled');
        }
    });

    increase.addEventListener('click', function () {
        let quantity = parseInt(quantityInput.value);
        quantity++;
        quantityInput.value = quantity;
        let p = parseInt(priceEle.getAttribute('price')) * quantity;
        priceEle.innerHTML = currencyFormatter(p);
        if (quantity > 1) {
            decrease.classList.remove('disabled');
        }
        total();
        const id = item.querySelector('input[name="id"]').value
        changeQuantity(id, quantity)
    });

    quantityInput.addEventListener('input', function () {
        if (!parseInt(this.value) || parseInt(this.value) < 1) {
            this.value = '';
        } else {
            let quantity = parseInt(this.value);
            let p = parseInt(priceEle.getAttribute('price')) * quantity;
            priceEle.innerHTML = currencyFormatter(p);
            if (quantity > 1) decrease.classList.remove('disabled');
            else decrease.classList.add('disabled');
            total();
            const id = item.querySelector('input[name="id"]').value
            changeQuantity(id, quantity)
        }
    });
});

function orderNums() {
    const nums = document.querySelectorAll('.oder-num')
    nums.forEach((num, i) => {
        num.innerHTML = i + 1 + ""
    })
}

btnDeletes.forEach((item) => {
    item.addEventListener('click', function () {
        if (confirm('Bạn chắc chắn muốn xóa sản này khỏi giỏ hàng') === true) {
            const id = this.closest('li').querySelector('input[name="id"]').value
            const head = document.querySelector('.head h1')
            const quantityCart = document.querySelector('.controls .quantity')
            $.ajax({
                url: '/cart/delete',
                type: 'POST',
                data: {
                    productId: id,
                    action: "delete"
                },
                success: function (data) {
                    item.closest('li').remove()
                    head.innerHTML = `Giỏ hàng của bạn có ${data.quantity} sản phẩm`
                    quantityCart.innerHTML = data.quantity
                    orderNums()
                    total()
                    const cartEmpty = document.querySelector('.cart-empty')
                    const cart = document.querySelector('.carts')
                    if (data.quantity === 0) {
                        cartEmpty.style.display = 'flex'
                        cart.style.display = 'none'
                    } else {
                        cartEmpty.style.display = 'none'
                        cart.style.display = 'block'
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        }
    })
});


const changeQuantity = debounce((id, quantity) => {
    $.ajax({
        url: `/cart/change-quantity`,
        type: 'POST',
        data: {
            productId: id,
            quantity,
            action: 'change-quantity'
        },
        success: function () {},
        error: function () {
            alert("error");
        }
    })
}, 1000)


btnDeleteAll.addEventListener('click', function () {
    if (confirm('Bạn chắc chắn muốn xóa tất cả mặt hàng') === true) {
        $.ajax({
            url: '/cart/delete',
            type: 'POST',
            data: {
                action: "delete-all"
            },
            success: function () {
                const cartEmpty = document.querySelector('.cart-empty');
                const cart = document.querySelector('.carts');
                const quantityCart = document.querySelector('.controls .quantity');
                cartEmpty.style.display = 'flex';
                cart.style.display = 'none';
                quantityCart.innerHTML = '0';
            },
            error: function () {
                alert("error");
            }
        })
    }
})

btnOrder.addEventListener('click', function () {
    if (confirm('Xác nhận đặt mua') === true) {
        $.ajax({
            url: '/TikiClone/cart-ajax',
            type: 'POST',
            data: {
                action: "ordered"
            },
            success: function () {
                window.location.href = 'home';
            },
            error: function () {
                alert("error");
            }
        })
    }
})
