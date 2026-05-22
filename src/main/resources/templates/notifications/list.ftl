<#import "../layout/base.ftl" as layout>
<@layout.page title="Уведомления">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Уведомления</h2>
        <#if unreadCount?? && unreadCount?number gt 0>
            <button class="btn btn-outline-primary btn-sm" onclick="markAllRead()">
                Прочитать все (${unreadCount})
            </button>
        </#if>
    </div>

    <#if notifications?size == 0>
        <p>У вас пока нет уведомлений.</p>
    <#else>
        <div class="list-group" id="notificationsList">
            <#list notifications as notification>
                <div class="list-group-item ${notification.isRead?then('', 'list-group-item-primary')}"
                     data-id="${notification.id}">
                    <div class="d-flex justify-content-between">
                        <div>
                            <strong>${notification.title}</strong>
                            <p class="mb-1">${notification.message!''}</p>
                            <small class="text-muted">${notification.createdAt?string("dd.MM.yyyy HH:mm")}</small>
                        </div>
                        <#if !notification.isRead>
                            <button class="btn btn-sm btn-outline-success" onclick="markRead(${notification.id})">
                                ✓
                            </button>
                        </#if>
                    </div>
                    <#if notification.relatedOrderId??>
                        <a href="/orders/${notification.relatedOrderId}" class="btn btn-link btn-sm">
                            Перейти к заказу
                        </a>
                    </#if>
                </div>
            </#list>
        </div>
    </#if>

    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <script src="/js/notifications.js"></script>

</@layout.page>