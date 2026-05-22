<#import "../layout/base.ftl" as layout>
<@layout.page title="Отзывы о мастере ${master.firstName!} ${master.lastName!}">

    <div class="row">
        <div class="col-md-8">
            <h2>Отзывы о мастере ${master.firstName!} ${master.lastName!}</h2>

            <div class="card mb-4">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <span class="display-1">${master.rating}</span>
                            <span class="h3">★</span>
                            <p class="text-muted">средний рейтинг</p>
                        </div>
                        <div class="col-md-8">
                            <p><strong>Всего отзывов:</strong> ${reviewsCount}</p>
                            <p><strong>Специализации:</strong>
                                <#list master.specializations as spec>
                                    ${spec.name}<#if spec_has_next>, </#if>
                                </#list>
                            </p>
                            <a href="/masters/${master.id}" class="btn btn-outline-primary">← Вернуться к профилю</a>
                            <#if Session.SPRING_SECURITY_CONTEXT??>
                                <#assign auth = Session.SPRING_SECURITY_CONTEXT.authentication>
                                <#if auth.authorities?seq_contains("ROLE_CUSTOMER")>
                                    <a href="/orders/create-for-master/${master.id}" class="btn btn-primary">Заказать</a>
                                </#if>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>

            <h4>Все отзывы</h4>

            <#if reviews?size == 0>
                <div class="alert alert-info">У мастера пока нет отзывов. Будьте первым!</div>
            <#else>
                <div class="list-group">
                    <#list reviews as review>
                        <div class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong>${review.author.firstName!} ${review.author.lastName!}</strong>
                                    <span class="ms-2">
                                        <#list 1..review.rating as i>⭐</#list>
                                        <#list review.rating+1..5 as i>☆</#list>
                                    </span>
                                </div>
                                <small class="text-muted">${review.createdAt}</small>
                            </div>
                            <#if review.comment?? && review.comment?has_content>
                                <p class="mb-2 mt-2">"${review.comment}"</p>
                            </#if>
                            <small class="text-muted">
                                <a href="/orders/${review.order.id}">Заказ #${review.order.id}</a>
                            </small>
                        </div>
                    </#list>
                </div>
            </#if>
        </div>
    </div>

</@layout.page>