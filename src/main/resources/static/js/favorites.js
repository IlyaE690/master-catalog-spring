async function addToFavorites(masterId) {
    const response = await fetch('/favorites/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'masterId=' + masterId
    });
    const data = await response.json();
    alert(data.message);
}

async function removeFromFavorites(masterId) {
    const response = await fetch('/favorites/remove', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'masterId=' + masterId
    });
    const data = await response.json();
    if (data.success) {
        location.reload();
    }
}