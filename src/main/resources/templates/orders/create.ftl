<#import "../layout/base.ftl" as layout>
<@layout.page title="Создать заказ">

    <h2>Создать заказ</h2>

    <form method="post" action="/orders/new">
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

        <button type="submit" class="btn btn-primary">Создать заказ</button>
    </form>

</@layout.page>