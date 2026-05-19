<#import "../layout/base.ftl" as layout>
<@layout.page title="Заказ #${order.id}">

    <h2>${order.title}</h2>

    <div class="card mb-3">
        <div class="card-body">
            <p><strong>Статус:</strong> ${order.status}</p>
            <p><strong>Специализация:</strong> ${order.specialization.name}</p>
            <p><strong>Адрес:</strong> ${order.address}</p>
            <p><strong>Описание:</strong> ${order.description!'-'}</p>
            <p><strong>Дата:</strong> ${order.scheduledDate}</p>
            <#if order.price??>
                <p><strong>Цена:</strong> ${order.price} ₽</p>
                <#if priceInUsd?? && (priceInUsd > 0)>
                    <p><strong>Ориентир в USD:</strong> ${priceInUsd} $</p>
                </#if>
            </#if>
            <#if order.imageUrl??>
                <p><strong>Фото проблемы:</strong></p>
                <img src="${order.imageUrl}" alt="Фото заявки" class="img-fluid rounded" style="max-height: 320px;">
            </#if>

            <#if order.master??>
                <p><strong>Мастер:</strong> ${order.master.firstName!} ${order.master.lastName!}</p>
            </#if>
        </div>
    </div>

    <div class="d-flex gap-2">
        <#if isCustomer && order.status == "NEW">
            <form method="post" action="/orders/${order.id}/cancel">
                <button class="btn btn-danger">Отменить</button>
            </form>
        </#if>

        <#if isMaster && order.status == "ASSIGNED">
            <form method="post" action="/orders/${order.id}/start">
                <button class="btn btn-primary">Начать работу</button>
            </form>
        </#if>

        <#if isMaster && order.status == "IN_PROGRESS">
            <form method="post" action="/orders/${order.id}/complete">
                <button class="btn btn-success">Завершить</button>
            </form>
        </#if>
    </div>

    <a href="javascript:history.back()" class="btn btn-secondary mt-3">Назад</a>

</@layout.page>