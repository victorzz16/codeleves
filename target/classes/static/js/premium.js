(() => {
    let selected = 'TARJETA';
    const methods = document.querySelectorAll('#paymentMethods button');
    const fields = document.getElementById('paymentFields');
    const pay = document.getElementById('btnPay');
    const modal = document.getElementById('paymentModal');

    const renderFields = () => {
        if (!fields) return;
        if (selected === 'TARJETA') {
            fields.innerHTML = `
                <div class="checkout-card-number-wrap">
                    <input id="cardNumber" inputmode="numeric" maxlength="19" placeholder="Número de tarjeta" autocomplete="cc-number">
                    <div class="checkout-card-brands"><span>VISA</span><span>MC</span><span>AMEX</span></div>
                </div>
                <div class="checkout-two-fields">
                    <input id="cardExpiry" inputmode="numeric" maxlength="5" placeholder="MM / AA" autocomplete="cc-exp">
                    <input id="cardCvv" inputmode="numeric" maxlength="3" placeholder="CVC" autocomplete="cc-csc">
                </div>`;
        } else if (selected === 'PAGO_EFECTIVO') {
            fields.innerHTML = `
                <div class="checkout-alt-payment">
                    <div class="alt-payment-icon">PE</div>
                    <div><b>Código de Pago Efectivo</b><p>Al confirmar se generará un código CIP simulado válido para esta demostración.</p></div>
                </div>`;
        } else {
            const nombre = selected === 'YAPE' ? 'Yape' : 'Plin';
            fields.innerHTML = `
                <div class="checkout-alt-payment">
                    <div class="alt-payment-icon">${selected === 'YAPE' ? 'Y' : 'P'}</div>
                    <div><b>Pagar con ${nombre}</b><p>Ingresa un número celular para simular la aprobación del pago.</p></div>
                </div>
                <input id="phoneNumber" inputmode="numeric" maxlength="9" placeholder="Número de celular (9 dígitos)">`;
        }
    };

    renderFields();

    methods.forEach(button => button.addEventListener('click', () => {
        methods.forEach(x => x.classList.remove('selected'));
        button.classList.add('selected');
        selected = button.dataset.method;
        renderFields();
    }));

    document.addEventListener('input', event => {
        if (event.target.id === 'cardNumber') {
            event.target.value = event.target.value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
        }
        if (event.target.id === 'cardExpiry') {
            let value = event.target.value.replace(/\D/g, '').slice(0, 4);
            if (value.length > 2) value = value.slice(0, 2) + '/' + value.slice(2);
            event.target.value = value;
        }
        if (event.target.id === 'cardCvv' || event.target.id === 'phoneNumber') {
            event.target.value = event.target.value.replace(/\D/g, '');
        }
    });

    const validate = () => {
        if (selected === 'TARJETA') {
            const number = document.getElementById('cardNumber')?.value.replace(/\s/g, '') || '';
            const expiry = document.getElementById('cardExpiry')?.value || '';
            const cvv = document.getElementById('cardCvv')?.value || '';
            if (number.length < 16 || !/^\d{2}\/\d{2}$/.test(expiry) || cvv.length !== 3) {
                throw new Error('Completa correctamente los datos de la tarjeta.');
            }
        }
        if (selected === 'YAPE' || selected === 'PLIN') {
            const phone = document.getElementById('phoneNumber')?.value || '';
            if (phone.length !== 9) throw new Error('Ingresa un número celular válido de 9 dígitos.');
        }
    };

    pay?.addEventListener('click', async () => {
        try {
            validate();
        } catch (error) {
            alert(error.message);
            return;
        }

        pay.disabled = true;
        pay.textContent = 'Procesando pago...';
        try {
            const response = await fetch('/api/pagos/simular', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({metodo: selected})
            });
            const data = await response.json();
            if (!response.ok || !data.ok) throw new Error(data.mensaje || 'No se pudo procesar el pago.');
            document.getElementById('paymentOperation').textContent = 'Operación: ' + data.operacion + ' · Vigencia: ' + data.vigencia;
            modal.classList.add('show');
            modal.setAttribute('aria-hidden', 'false');
        } catch (error) {
            alert(error.message);
            pay.disabled = false;
            pay.textContent = 'Pagar S/ 30.00';
        }
    });

    document.getElementById('btnContinue')?.addEventListener('click', () => location.href = '/alumno/dashboard');
})();
