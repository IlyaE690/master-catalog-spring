async function markRead(notificationId) {
    const response = await fetch('/notifications/' + notificationId + '/read', {
        method: 'POST'
    });
    const data = await response.json();
    if (data.success) {
        const item = document.querySelector('[data-id="' + notificationId + '"]');
        if (item) {
            item.classList.remove('list-group-item-primary');
            item.querySelector('button')?.remove();
        }
    }
}

async function markAllRead() {
    const response = await fetch('/notifications/read-all', {
        method: 'POST'
    });
    const data = await response.json();
    if (data.success) {
        location.reload();
    }
}