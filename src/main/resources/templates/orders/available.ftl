<#import "../layout/base.ftl" as layout>
<@layout.page title="Доступные заказы">

    <h2>Доступные заказы</h2>

    <#if orders?size == 0>
        <p>Нет доступных заказов по вашим специализациям.</p>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <div class="list-group-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <strong>${order.title}</strong>
                            <br><small>${order.specialization.name} | ${order.address}</small>
                        </div>
                        <form method="post" action="/orders/${order.id}/accept">
                            <button class="btn btn-primary btn-sm">Откликнуться</button>
                        </form>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>