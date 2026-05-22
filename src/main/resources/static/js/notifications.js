function appendNotification(notification) {
    let listGroup = document.querySelector('#notificationsList, .list-group');
    if (!listGroup) {
        listGroup = document.createElement('div');
        listGroup.className = 'list-group';
        listGroup.id = 'notificationsList';
        document.querySelector('main .container, main')?.appendChild(listGroup);
    }

    const item = document.createElement('div');
    item.className = 'list-group-item list-group-item-primary';
    item.setAttribute('data-id', notification.id);
    const createdAt = notification.createdAt ? new Date(notification.createdAt).toLocaleString('ru-RU') : '';
    item.innerHTML = `
        <div class="d-flex justify-content-between">
            <div>
                <strong>${escapeHtml(notification.title)}</strong>
                <p class="mb-1">${escapeHtml(notification.message)}</p>
                <small class="text-muted">${createdAt}</small>
            </div>
            <button class="btn btn-sm btn-outline-success" onclick="markRead(${notification.id})">✓</button>
        </div>
        ${notification.relatedOrderId ? `<a href="/orders/${notification.relatedOrderId}" class="btn btn-link btn-sm">Перейти к заказу</a>` : ''}
    `;
    listGroup.prepend(item);
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>]/g, function(m) {
        if (m === '&') return '&amp;';
        if (m === '<') return '&lt;';
        if (m === '>') return '&gt;';
        return m;
    });
}

async function markRead(notificationId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const headers = {};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    const response = await fetch('/notifications/' + notificationId + '/read', {
        method: 'POST',
        headers: headers
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
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const headers = {};
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    const response = await fetch('/notifications/read-all', {
        method: 'POST',
        headers: headers
    });
    const data = await response.json();
    if (data.success) location.reload();
}

(function connectNotificationsSocket() {
    if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;
    if (window._stompConnected) return;
    window._stompConnected = true;

    const socket = new SockJS('/ws-notifications');
    const stompClient = Stomp.over(socket);
    stompClient.debug = () => {};
    stompClient.connect({}, function () {
        stompClient.subscribe('/user/queue/notifications', function (payload) {
            try {
                const notification = JSON.parse(payload.body);
                appendNotification(notification);
            } catch(e) {
                console.error('Ошибка парсинга уведомления', e);
            }
        });
    });
})();
