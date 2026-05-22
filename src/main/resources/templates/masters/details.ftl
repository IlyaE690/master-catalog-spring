<#import "../layout/base.ftl" as layout>
<@layout.page title="${master.firstName!} ${master.lastName!}">

    <div class="row">
        <div class="col-md-8">
            <h2>${master.firstName!} ${master.lastName!}</h2>
            <p>Рейтинг: ${master.rating} ★</p>
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