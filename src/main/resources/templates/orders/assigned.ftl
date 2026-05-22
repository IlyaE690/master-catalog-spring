<#import "../layout/base.ftl" as layout>
<@layout.page title="Мои заказы (мастер)">

    <h2>Мои заказы</h2>

    <#if orders?size == 0>
        <p>У вас пока нет назначенных заказов.</p>
        <a href="/orders/available" class="btn btn-primary">Доступные заказы</a>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <a href="/orders/${order.id}" class="list-group-item list-group-item-action">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <strong>${order.title}</strong>
                            <br>
                            <small>${order.address} | ${order.formattedScheduledDate!''}</small>
                        </div>
                        <span class="badge
                            <#if order.status == 'ASSIGNED'>bg-warning</#if>
                            <#if order.status == 'IN_PROGRESS'>bg-primary</#if>
                            <#if order.status == 'COMPLETED'>bg-success</#if>
                            <#if order.status == 'CANCELLED'>bg-danger</#if>
                        ">
                            <#if order.status == 'ASSIGNED'>Назначен</#if>
                            <#if order.status == 'IN_PROGRESS'>В работе</#if>
                            <#if order.status == 'COMPLETED'>Завершён</#if>
                            <#if order.status == 'CANCELLED'>Отменён</#if>
                        </span>
                    </div>
                </a>
            </#list>
        </div>
    </#if>

</@layout.page>