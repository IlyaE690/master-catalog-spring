async function addToFavorites(masterId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const formData = new URLSearchParams();
    formData.append('masterId', masterId);

    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    };
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    try {
        const response = await fetch('/favorites/add', {
            method: 'POST',
            headers: headers,
            body: formData.toString()
        });

        if (response.redirected) {
            window.location.href = response.url;
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при добавлении в избранное');
    }
}