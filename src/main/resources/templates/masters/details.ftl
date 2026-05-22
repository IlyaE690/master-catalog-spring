<#import "../layout/base.ftl" as layout>
<@layout.page title="${master.firstName!} ${master.lastName!}">

    <div class="row">
        <div class="col-md-8">
            <h2>${master.firstName!} ${master.lastName!}</h2>

            <div class="mb-3">
                <span class="h4">Рейтинг: ${master.rating} ★</span>
                <a href="/reviews/master/${master.id}" class="btn btn-outline-secondary btn-sm ms-3">
                    Все отзывы (${reviewsCount!0})
                </a>
            </div>

            <p>Телефон: ${master.phone!}</p>
            <p>Email: ${master.email}</p>

            <h5>Специализации:</h5>
            <ul>
                <#if master.specializations?? && master.specializations?size gt 0>
                    <#list master.specializations as spec>
                        <li>${spec.name} — от ${spec.basePrice} ₽</li>
                    </#list>
                <#else>
                    <li>Не указаны</li>
                </#if>
            </ul>

            <h5 class="mt-4">Последние отзывы</h5>
            <#if reviews?size == 0>
                <p class="text-muted">У мастера пока нет отзывов</p>
            <#else>
                <div class="list-group">
                    <#list reviews as review>
                        <div class="list-group-item">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <strong>${review.author.firstName!} ${review.author.lastName!}</strong>
                                    <span class="ms-2">
                                        <#list 1..review.rating as i>⭐</#list>
                                        <#list review.rating+1..5 as i>☆</#list>
                                    </span>
                                </div>
                                <small class="text-muted">${review.createdAt?string("dd.MM.yyyy")}</small>
                            </div>
                            <#if review.comment?? && review.comment?has_content>
                                <p class="mb-0 mt-2">"${review.comment}"</p>
                            </#if>
                            <small class="text-muted">Заказ #${review.order.id}</small>
                        </div>
                    </#list>
                </div>
                <#if reviews?size gt 3>
                    <a href="/reviews/master/${master.id}" class="btn btn-link mt-2">Показать все →</a>
                </#if>
            </#if>

            <div class="mt-3">
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <#assign auth = Session.SPRING_SECURITY_CONTEXT.authentication>
                    <#if auth.authorities?seq_contains("ROLE_CUSTOMER")>
                        <a href="/orders/create-for-master/${master.id}" class="btn btn-primary">Заказать у этого мастера</a>

                        <#if isFavorite?? && isFavorite == true>
                            <form method="post" action="/favorites/remove" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="masterId" value="${master.id}"/>
                                <button type="submit" class="btn btn-outline-danger">Удалить из избранного</button>
                            </form>
                        <#else>
                            <form method="post" action="/favorites/add" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="masterId" value="${master.id}"/>
                                <button type="submit" class="btn btn-outline-primary">В избранное</button>
                            </form>
                        </#if>
                    </#if>
                </#if>
            </div>
        </div>
    </div>

    <a href="/masters" class="btn btn-secondary mt-3">← Назад к каталогу</a>

</@layout.page>