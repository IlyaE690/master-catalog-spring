<#import "../layout/base.ftl" as layout>
<@layout.page title="Заказ #${order.id}">

    <h2>${order.title}</h2>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <div class="card mb-3">
        <div class="card-body">
            <p><strong>Статус:</strong> ${order.status}</p>
            <p><strong>Специализация:</strong> ${order.specialization.name}</p>
            <p><strong>Адрес:</strong> ${order.address}</p>
            <p><strong>Описание:</strong> ${order.description!'-'}</p>
            <p><strong>Дата выполнения:</strong> ${order.formattedScheduledDate!'-'}</p>
            <p><strong>Создан:</strong> ${order.formattedCreatedAt!'-'}</p>
            <#if order.completedAt??>
                <p><strong>Завершён:</strong> ${order.formattedCompletedAt!'-'}</p>
            </#if>
            <#if order.price??>
                <p><strong>Цена:</strong> ${order.price} ₽</p>
                <#if priceInUsd??>
                    <p><small>≈ ${priceInUsd} USD</small></p>
                </#if>
            </#if>

            <#if order.imageUrl?? && order.imageUrl?has_content>
                <p><strong>Фото:</strong></p>
                <img src="${order.imageUrl}" class="img-fluid rounded" style="max-width: 100%; max-height: 400px;">
            </#if>

            <#if order.master??>
                <p><strong>Мастер:</strong> ${order.master.firstName!} ${order.master.lastName!}</p>
            </#if>
        </div>
    </div>

    <div class="d-flex gap-2">
        <#if isCustomer>
            <#if order.status == "NEW">
                <form method="post" action="/orders/${order.id}/cancel">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-danger">Отменить заказ</button>
                </form>
            </#if>

            <#if order.status == "COMPLETED">
                <#if order.review??>
                    <div class="alert alert-info">Отзыв оставлен: ${order.review.rating} ★</div>
                <#else>
                    <a href="/reviews/create/${order.id}" class="btn btn-outline-primary">Оставить отзыв</a>
                </#if>
            </#if>
        </#if>

        <#if isMaster>
            <#if order.status == "ASSIGNED">
                <form method="post" action="/orders/${order.id}/start">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-primary">Начать работу</button>
                </form>
                <form method="post" action="/orders/${order.id}/reject">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-warning">Отказаться</button>
                </form>
            </#if>
            <#if order.status == "IN_PROGRESS">
                <form method="post" action="/orders/${order.id}/complete">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-success">Завершить заказ</button>
                </form>
            </#if>
        </#if>
    </div>

    <div class="mt-3">
        <a href="<#if isMaster>/orders/assigned<#elseif isCustomer>/orders/my<#else>/</#if>" class="btn btn-secondary">Назад</a>
        <a href="/" class="btn btn-outline-secondary">На главную</a>
    </div>

</@layout.page>