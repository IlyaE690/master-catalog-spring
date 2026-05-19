<#import "../layout/base.ftl" as layout>
<@layout.page title="Создать заказ">

    <h2>Создать заказ</h2>

    <form method="post" action="/orders/new" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="specializationId" class="form-label">Тип услуги</label>
            <select class="form-select" id="specializationId" name="specializationId" required>
                <option value="">Выберите...</option>
                <#list specializations as spec>
                    <option value="${spec.id}">${spec.name} (от ${spec.basePrice} ₽)</option>
                </#list>
            </select>
        </div>

        <div class="mb-3">
            <label for="title" class="form-label">Заголовок</label>
            <input type="text" class="form-control" id="title" name="title"
                   placeholder="Починить слив в ванной" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Описание</label>
            <textarea class="form-control" id="description" name="description"
                      rows="4" placeholder="Подробно опишите проблему"></textarea>
        </div>

        <div class="mb-3">
            <label for="address" class="form-label">Адрес</label>
            <input type="text" class="form-control" id="address" name="address"
                   placeholder="ул. Ленина 5, кв 42, 3 этаж, домофон 12" required>
        </div>

        <div class="mb-3">
            <label for="scheduledDate" class="form-label">Желаемая дата</label>
            <input type="datetime-local" class="form-control" id="scheduledDate" name="scheduledDate" required>
        </div>


        <div class="mb-3">
            <label for="minRating" class="form-label">Минимальный рейтинг мастера</label>
            <select class="form-select" id="minRating" name="minRating">
                <option value="">Не важно</option>
                <option value="4.0">От 4.0</option>
                <option value="4.5">От 4.5</option>
                <option value="4.8">От 4.8</option>
            </select>
        </div>

        <button type="button" id="aiSuggestBtn" class="btn btn-outline-secondary mb-3">Подобрать мастера с ИИ</button>
        <div id="aiBox" class="alert alert-secondary" style="display:none;">
            <p><strong>Сформированный промпт для ИИ:</strong></p>
            <pre id="aiPrompt" ></pre>
            <p class="mt-2"><strong>Подходящие мастера:</strong></p>
            <ul id="aiMasters"></ul>
        </div>

        <div class="mb-3">
            <label for="orderPhoto" class="form-label">Фото проблемы (опционально)</label>
            <input type="file" class="form-control" id="orderPhoto" name="orderPhoto" accept="image/*">
            <small class="text-muted">При настройке Cloudinary фото будет загружено и прикреплено к заявке</small>
        </div>

        <button type="submit" class="btn btn-primary">Создать заказ</button>
    </form>

    <div id="order-map" class="mt-3"></div>
    <small class="text-muted">Карта по адресу заказа (Yandex Maps API)</small>

    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU&apikey=${yandexApiKey!""}"></script>
    <script src="/js/order-map.js"></script>


    <script src="/js/ai-order.js"></script>
</@layout.page>