document.addEventListener('DOMContentLoaded', function() {
    const usernameInput = document.getElementById('username');
    const emailInput = document.getElementById('email');

    if (usernameInput) {
        usernameInput.addEventListener('blur', async function() {
            const username = this.value;
            if (username.length < 3) return;
            const response = await fetch('/api/validate/username?username=' + username);
            const data = await response.json();
            if (data.exists) {
                showFieldError(usernameInput, 'Логин занят');
            } else {
                clearFieldError(usernameInput);
            }
        });
    }

    if (emailInput) {
        emailInput.addEventListener('blur', async function() {
            const email = this.value;
            if (!email.includes('@')) return;
            const response = await fetch('/api/validate/email?email=' + email);
            const data = await response.json();
            if (data.exists) {
                showFieldError(emailInput, 'Email занят');
            } else {
                clearFieldError(emailInput);
            }
        });
    }
});

function showFieldError(input, message) {
    input.classList.add('is-invalid');
    let feedback = input.nextElementSibling;
    if (!feedback || !feedback.classList.contains('invalid-feedback')) {
        feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        input.parentNode.appendChild(feedback);
    }
    feedback.textContent = message;
}

function clearFieldError(input) {
    input.classList.remove('is-invalid');
    const feedback = input.nextElementSibling;
    if (feedback && feedback.classList.contains('invalid-feedback')) {
        feedback.textContent = '';
    }
}