<#import "../layout/base.ftl" as layout>
<@layout.page title="Оставить отзыв">

    <h2>Оставить отзыв о заказе #${order.id}</h2>
    <p><strong>Мастер:</strong> ${order.master.firstName!} ${order.master.lastName!}</p>
    <p><strong>Услуга:</strong> ${order.specialization.name}</p>

    <form method="post" action="/reviews/create/${order.id}">
        <div class="mb-3">
            <label for="rating" class="form-label">Оценка</label>
            <select class="form-select" id="rating" name="rating" required>
                <option value="5">⭐⭐⭐⭐⭐ (5)</option>
                <option value="4">⭐⭐⭐⭐ (4)</option>
                <option value="3">⭐⭐⭐ (3)</option>
                <option value="2">⭐⭐ (2)</option>
                <option value="1">⭐ (1)</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="comment" class="form-label">Комментарий</label>
            <textarea class="form-control" id="comment" name="comment" rows="4"
                      placeholder="Опишите ваш опыт работы с мастером"></textarea>
        </div>

        <button type="submit" class="btn btn-primary">Оставить отзыв</button>
        <a href="/orders/${order.id}" class="btn btn-secondary">Отмена</a>
    </form>

</@layout.page>