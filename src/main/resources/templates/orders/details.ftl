<#import "../layout/base.ftl" as layout>
<@layout.page title="Заказ #${order.id}">

    <h2>${order.title}</h2>

    <div class="card mb-3">
        <div class="card-body">
            <p><strong>Статус:</strong> ${order.status}</p>
            <p><strong>Специализация:</strong> ${order.specialization.name}</p>
            <p><strong>Адрес:</strong> ${order.address}</p>
            <p><strong>Описание:</strong> ${order.description!'-'}</p>
            <p><strong>Дата:</strong> ${order.formattedScheduledDate!'-'}</p>
            <p><strong>Создан:</strong> ${order.formattedCreatedAt!'-'}</p>
            <#if order.price??>
                <p><strong>Цена:</strong> ${order.price} ₽</p>
                <#if priceInUsd?? && (priceInUsd > 0)>
                    <p><strong>Ориентир в USD:</strong> ${priceInUsd} $</p>
                </#if>
            </#if>

            <#if order.imageUrl?? && order.imageUrl?has_content>
                <p><strong>Фото проблемы:</strong></p>
                <img src="${order.imageUrl}" alt="Фото заявки" class="img-fluid rounded" style="max-width: 100%; max-height: 400px;">
            </#if>

            <#if order.master??>
                <p><strong>Мастер:</strong> ${order.master.firstName!} ${order.master.lastName!}</p>
            </#if>
        </div>
    </div>

    <div class="d-flex gap-2 flex-wrap">
        <#if isCustomer && order.status == "NEW">
            <form method="post" action="/orders/${order.id}/cancel">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-danger">Отменить заказ</button>
            </form>
        </#if>

        <#if isMaster && order.status == "ASSIGNED">
            <form method="post" action="/orders/${order.id}/start">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-primary">Начать работу</button>
            </form>
            <form method="post" action="/orders/${order.id}/reject">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-warning" onclick="return confirm('Отказаться от заказа?')">Отказаться</button>
            </form>
        </#if>

        <#if isMaster && order.status == "IN_PROGRESS">
            <form method="post" action="/orders/${order.id}/complete">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-success">Завершить заказ</button>
            </form>
        </#if>

        <#if isCustomer && order.status == "COMPLETED" && order.master??>
            <a href="/reviews/create/${order.id}" class="btn btn-outline-primary">Оставить отзыв</a>
        </#if>
    </div>

    <button onclick="window.location.href='/'" class="btn btn-secondary mt-3">На главную</button>
    <button onclick="window.history.back()" class="btn btn-outline-secondary mt-3">Назад</button>

</@layout.page>