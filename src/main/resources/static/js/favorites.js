async function addToFavorites(masterId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
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
            body: 'masterId=' + masterId
        });
        const data = await response.json();
        if (data.success) {
            alert(data.message);
        } else {
            alert(data.message || 'Ошибка при добавлении в избранное');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при добавлении в избранное');
    }
}

async function removeFromFavorites(masterId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    };
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    try {
        const response = await fetch('/favorites/remove', {
            method: 'POST',
            headers: headers,
            body: 'masterId=' + masterId
        });
        const data = await response.json();
        if (data.success) {
            location.reload();
        } else {
            alert(data.message || 'Ошибка при удалении из избранного');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при удалении из избранного');
    }
}
