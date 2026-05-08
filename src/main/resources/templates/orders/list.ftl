<#import "../layout/base.ftl" as layout>
<@layout.page title="Мои заказы">

    <h2>Мои заказы</h2>

    <#if orders?size == 0>
        <p>У вас пока нет заказов.</p>
        <a href="/orders/new" class="btn btn-primary">Создать заказ</a>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <a href="/orders/${order.id}" class="list-group-item list-group-item-action">
                    <div class="d-flex justify-content-between">
                        <strong>${order.title}</strong>
                        <span class="badge
                            <#if order.status == 'NEW'>bg-info</#if>
                            <#if order.status == 'ASSIGNED'>bg-warning</#if>
                            <#if order.status == 'IN_PROGRESS'>bg-primary</#if>
                            <#if order.status == 'COMPLETED'>bg-success</#if>
                            <#if order.status == 'CANCELLED'>bg-danger</#if>
                        ">
                            ${order.status}
                        </span>
                    </div>
                    <small class="text-muted">${order.specialization.name} | ${order.createdAt}</small>
                </a>
            </#list>
        </div>
    </#if>

</@layout.page>