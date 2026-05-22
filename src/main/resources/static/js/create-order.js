document.addEventListener('DOMContentLoaded', function() {
    const orderForm = document.getElementById('orderForm');
    const submitBtn = document.getElementById('submitBtn');
    const specializationSelect = document.getElementById('specializationId');
    const titleInput = document.getElementById('title');
    const descriptionInput = document.getElementById('description');
    const addressInput = document.getElementById('address');
    const scheduledDateInput = document.getElementById('scheduledDate');

    const specializationError = document.getElementById('specializationError');
    const titleError = document.getElementById('titleError');
    const descriptionError = document.getElementById('descriptionError');
    const addressError = document.getElementById('addressError');
    const dateError = document.getElementById('dateError');

    function validateForm() {
        let isValid = true;

        if (!specializationSelect || !specializationSelect.value) {
            if (specializationError) specializationError.style.display = 'block';
            isValid = false;
        } else {
            if (specializationError) specializationError.style.display = 'none';
        }

        const title = titleInput ? titleInput.value.trim() : '';
        if (title.length < 3) {
            if (titleError) {
                titleError.textContent = 'Заголовок должен содержать минимум 3 символа';
                titleError.style.display = 'block';
            }
            if (titleInput) titleInput.classList.add('is-invalid');
            isValid = false;
        } else {
            if (titleError) titleError.style.display = 'none';
            if (titleInput) titleInput.classList.remove('is-invalid');
        }

        const description = descriptionInput ? descriptionInput.value.trim() : '';
        if (description.length < 5) {
            if (descriptionError) {
                descriptionError.textContent = 'Описание должно содержать минимум 5 символов';
                descriptionError.style.display = 'block';
            }
            if (descriptionInput) descriptionInput.classList.add('is-invalid');
            isValid = false;
        } else {
            if (descriptionError) descriptionError.style.display = 'none';
            if (descriptionInput) descriptionInput.classList.remove('is-invalid');
        }

        const address = addressInput ? addressInput.value.trim() : '';
        if (address.length < 5) {
            if (addressError) {
                addressError.textContent = 'Введите корректный адрес (минимум 5 символов)';
                addressError.style.display = 'block';
            }
            if (addressInput) addressInput.classList.add('is-invalid');
            isValid = false;
        } else {
            if (addressError) addressError.style.display = 'none';
            if (addressInput) addressInput.classList.remove('is-invalid');
        }

        if (scheduledDateInput && scheduledDateInput.value) {
            const scheduledDate = new Date(scheduledDateInput.value);
            const now = new Date();
            now.setSeconds(0, 0);

            if (scheduledDate < now) {
                if (dateError) {
                    dateError.textContent = 'Дата выполнения не может быть в прошлом';
                    dateError.style.display = 'block';
                }
                if (scheduledDateInput) scheduledDateInput.classList.add('is-invalid');
                isValid = false;
            } else {
                if (dateError) dateError.style.display = 'none';
                if (scheduledDateInput) scheduledDateInput.classList.remove('is-invalid');
            }
        } else if (scheduledDateInput && !scheduledDateInput.value) {
            if (dateError) {
                dateError.textContent = 'Выберите дату выполнения';
                dateError.style.display = 'block';
            }
            if (scheduledDateInput) scheduledDateInput.classList.add('is-invalid');
            isValid = false;
        }

        if (submitBtn) {
            submitBtn.disabled = !isValid;
        }

        return isValid;
    }

    function setupLiveValidation(input, errorElement, validateFn) {
        if (!input) return;

        input.addEventListener('input', function() {
            if (validateFn()) {
                input.classList.remove('is-invalid');
                if (errorElement) errorElement.style.display = 'none';
            } else {
                input.classList.add('is-invalid');
                if (errorElement) errorElement.style.display = 'block';
            }
            validateForm();
        });

        input.addEventListener('blur', function() {
            validateForm();
        });
    }

    if (titleInput) {
        setupLiveValidation(titleInput, titleError, function() {
            return titleInput.value.trim().length >= 3;
        });
    }

    if (descriptionInput) {
        setupLiveValidation(descriptionInput, descriptionError, function() {
            return descriptionInput.value.trim().length >= 5;
        });
    }

    if (addressInput) {
        setupLiveValidation(addressInput, addressError, function() {
            return addressInput.value.trim().length >= 5;
        });
    }

    if (specializationSelect) {
        specializationSelect.addEventListener('change', function() {
            if (specializationSelect.value) {
                specializationSelect.classList.remove('is-invalid');
                if (specializationError) specializationError.style.display = 'none';
            } else {
                specializationSelect.classList.add('is-invalid');
                if (specializationError) specializationError.style.display = 'block';
            }
            validateForm();
        });
    }

    if (scheduledDateInput) {
        scheduledDateInput.addEventListener('change', function() {
            validateForm();
        });
        scheduledDateInput.addEventListener('input', function() {
            validateForm();
        });
    }

    if (orderForm) {
        orderForm.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                alert('Пожалуйста, заполните все поля корректно');
                return false;
            }
        });
    }

    validateForm();
});