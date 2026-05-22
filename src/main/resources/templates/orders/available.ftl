<#import "../layout/base.ftl" as layout>
<@layout.page title="Доступные заказы">

    <h2>Доступные заказы</h2>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <#if orders?size == 0>
        <p>Нет доступных заказов по вашим специализациям.</p>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <div class="list-group-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <strong>${order.title}</strong>
                            <br><small>${order.specialization.name} | ${order.address} | ${order.formattedScheduledDate!''}</small>
                            <br><small class="text-muted">Клиент: ${order.customer.username}</small>
                        </div>
                        <form method="post" action="/orders/${order.id}/accept">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button class="btn btn-primary btn-sm">Принять заказ</button>
                        </form>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>