<#import "../layout/base.ftl" as layout>
<@layout.page title="Создать заказ для мастера">

    <h2>Создать заказ для мастера: ${targetMasterName}</h2>

    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <form method="post" action="/orders/create-with-master" enctype="multipart/form-data" id="orderForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="masterId" value="${targetMasterId}"/>

        <div class="mb-3">
            <label for="specializationId" class="form-label">Тип услуги <span class="text-danger">*</span></label>
            <select class="form-select" id="specializationId" name="specializationId" required>
                <option value="">Выберите...</option>
                <#list specializations as spec>
                    <option value="${spec.id}">${spec.name} (от ${spec.basePrice} ₽)</option>
                </#list>
            </select>
        </div>

        <div class="mb-3">
            <label for="title" class="form-label">Заголовок <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="title" name="title" placeholder="Например: Починить кран" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Описание <span class="text-danger">*</span></label>
            <textarea class="form-control" id="description" name="description" rows="4" placeholder="Подробно опишите проблему" required></textarea>
        </div>

        <div class="mb-3">
            <label for="address" class="form-label">Адрес <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="address" name="address" placeholder="г. Казань, ул. Баумана, 10" required>
        </div>

        <div class="mb-3">
            <label for="scheduledDate" class="form-label">Желаемая дата <span class="text-danger">*</span></label>
            <input type="datetime-local" class="form-control" id="scheduledDate" name="scheduledDate" required>
        </div>

        <div class="mb-3">
            <label for="orderPhoto" class="form-label">Фото проблемы (опционально)</label>
            <input type="file" class="form-control" id="orderPhoto" name="orderPhoto" accept="image/*">
        </div>

        <button type="submit" class="btn btn-primary">Отправить заказ мастеру</button>
        <a href="/masters/${targetMasterId}" class="btn btn-secondary">Отмена</a>
    </form>

    <div id="order-map" style="height: 400px; width: 100%; margin-top: 20px;"></div>

    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU&apikey=${yandexApiKey!""}"></script>
    <script src="/js/order-map.js"></script>
    <script src="/js/create-order.js"></script>

</@layout.page>