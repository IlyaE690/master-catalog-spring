function appendNotification(notification) {
    let listGroup = document.querySelector('.list-group');
    if (!listGroup) {
        listGroup = document.createElement('div');
        listGroup.className = 'list-group';
        document.querySelector('main .container, main')?.appendChild(listGroup);
    }

    const item = document.createElement('div');
    item.className = 'list-group-item list-group-item-primary';
    item.setAttribute('data-id', notification.id);
    item.innerHTML = `
        <div class="d-flex justify-content-between">
            <div>
                <strong>${notification.title}</strong>
                <p class="mb-1">${notification.message}</p>
                <small class="text-muted">${notification.createdAt}</small>
            </div>
            <button class="btn btn-sm btn-outline-success" onclick="markRead(${notification.id})">✓</button>
        </div>
        ${notification.relatedOrderId ? `<a href="/orders/${notification.relatedOrderId}" class="btn btn-link btn-sm">Перейти к заказу</a>` : ''}
    `;
    listGroup.prepend(item);
}

async function markRead(notificationId) {
    const response = await fetch('/notifications/' + notificationId + '/read', { method: 'POST' });
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
    const response = await fetch('/notifications/read-all', { method: 'POST' });
    const data = await response.json();
    if (data.success) location.reload();
}

(function connectNotificationsSocket() {
    if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;
    const socket = new SockJS('/ws-notifications');
    const stompClient = Stomp.over(socket);
    stompClient.debug = () => {};
    stompClient.connect({}, function () {
        stompClient.subscribe('/user/queue/notifications', function (payload) {
            appendNotification(JSON.parse(payload.body));
        });
    });
})();
