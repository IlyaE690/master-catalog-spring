<#import "../layout/base.ftl" as layout>
<@layout.page title="Модерация отзывов">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Отзывы</h2>
    </div>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>

    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Заказ</th>
                <th>Автор</th>
                <th>На кого</th>
                <th>Оценка</th>
                <th>Комментарий</th>
                <th>Дата</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <#list reviews as review>
                <tr>
                    <td>${review.id}</td>
                    <td><a href="/orders/${review.order.id}">#${review.order.id}</a></td>
                    <td>${review.author.firstName!} ${review.author.lastName!}</td>
                    <td>${review.targetUser.firstName!} ${review.targetUser.lastName!}</td>
                    <td>
                        <#assign ratingClass = "">
                        <#if review.rating >= 4>
                            <#assign ratingClass = "bg-success">
                        <#elseif review.rating >= 3>
                            <#assign ratingClass = "bg-warning">
                        <#else>
                            <#assign ratingClass = "bg-danger">
                        </#if>
                        <span class="badge ${ratingClass}">
                            ${review.rating} ★
                        </span>
                    </td>
                    <td style="max-width: 300px; overflow: hidden; text-overflow: ellipsis;">
                        ${review.comment!'-'}
                    </td>
                    <td>${review.createdAt?string("dd.MM.yyyy HH:mm")}</td>
                    <td>
                        <form method="post" action="/admin/reviews/${review.id}/delete" style="display: inline;"
                              onsubmit="return confirm('Удалить этот отзыв?')">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

</@layout.page>