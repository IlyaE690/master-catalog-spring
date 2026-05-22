async function addToFavorites(masterId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const headers = {'Content-Type': 'application/x-www-form-urlencoded'};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    const response = await fetch('/favorites/add', {
        method: 'POST',
        headers: headers,
        body: 'masterId=' + masterId
    });
    const data = await response.json();
    alert(data.message);
}

async function removeFromFavorites(masterId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const headers = {'Content-Type': 'application/x-www-form-urlencoded'};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    const response = await fetch('/favorites/remove', {
        method: 'POST',
        headers: headers,
        body: 'masterId=' + masterId
    });
    const data = await response.json();
    if (data.success) {
        location.reload();
    }
}
