<#import "../layout/base.ftl" as layout>
<@layout.page title="Создать заказ">

    <h2>Создать заказ</h2>

    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <form method="post" action="/orders/new" enctype="multipart/form-data" id="orderForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

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
            <input type="text" class="form-control" id="title" name="title"
                   placeholder="" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Описание <span class="text-danger">*</span></label>
            <textarea class="form-control" id="description" name="description"
                      rows="4" placeholder="Подробно опишите проблему" required></textarea>
        </div>

        <div class="mb-3">
            <label for="address" class="form-label">Адрес <span class="text-danger">*</span></label>
            <input type="text" class="form-control" id="address" name="address"
                   placeholder="" required>
        </div>

        <div class="mb-3">
            <label for="scheduledDate" class="form-label">Желаемая дата <span class="text-danger">*</span></label>
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

        <button type="button" id="aiSuggestBtn" class="btn btn-outline-secondary mb-3">Найти подходящих мастеров</button>

        <div id="aiMastersContainer" class="mt-3" style="display:none;">
            <h5>Рекомендованные мастера</h5>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Мастер</th>
                        <th>Рейтинг</th>
                        <th>Специализации</th>
                        <th>Выполнено заказов</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="aiMastersList">
                    </tbody>
                </table>
            </div>
        </div>

        <div id="aiBox" class="alert alert-info" style="display:none;">
            <p><strong>Совет:</strong> Выберите мастера из списка выше, чтобы отправить заказ напрямую ему.</p>
        </div>

        <div class="mb-3">
            <label for="orderPhoto" class="form-label">Фото проблемы (опционально)</label>
            <input type="file" class="form-control" id="orderPhoto" name="orderPhoto" accept="image/*">
        </div>

        <button type="submit" class="btn btn-primary" id="submitBtn">Создать заказ (без выбора мастера)</button>
    </form>

    <div id="order-map" style="height: 400px; width: 100%; margin-top: 20px;"></div>

    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU&apikey=${yandexApiKey!""}"></script>
    <script src="/js/order-map.js"></script>
    <script src="/js/ai-order.js"></script>
    <script src="/js/create-order.js"></script>

</@layout.page>