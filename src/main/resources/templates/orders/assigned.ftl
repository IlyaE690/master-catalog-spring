<#import "../layout/base.ftl" as layout>
<@layout.page title="Мои заказы (мастер)">

    <h2>Мои заказы</h2>

    <#if orders?size == 0>
        <p>У вас пока нет назначенных заказов.</p>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <a href="/orders/${order.id}" class="list-group-item list-group-item-action">
                    <div class="d-flex justify-content-between">
                        <strong>${order.title}</strong>
                        <span class="badge
                            <#if order.status == 'ASSIGNED'>bg-warning</#if>
                            <#if order.status == 'IN_PROGRESS'>bg-primary</#if>
                            <#if order.status == 'COMPLETED'>bg-success</#if>
                            <#if order.status == 'CANCELLED'>bg-danger</#if>
                        ">
                            ${order.status}
                        </span>
                    </div>
                    <small>${order.address} | ${order.scheduledDate}</small>
                </a>
            </#list>
        </div>
    </#if>

</@layout.page>