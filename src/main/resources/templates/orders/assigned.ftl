<#import "../layout/base.ftl" as layout>
<@layout.page title="Мои заказы (мастер)">

    <h2>Мои заказы</h2>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <#if orders?size == 0>
        <p>У вас пока нет назначенных заказов.</p>
        <a href="/orders/available" class="btn btn-primary">Доступные заказы</a>
    <#else>
        <div class="list-group">
            <#list orders as order>
                <div class="list-group-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <strong><a href="/orders/${order.id}">${order.title}</a></strong>
                            <br>
                            <small>${order.address} | ${order.formattedScheduledDate!''}</small>
                        </div>
                        <div>
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
                    </div>
                    <div class="mt-2">
                        <#if order.status == 'ASSIGNED'>
                            <form method="post" action="/orders/${order.id}/start" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-primary btn-sm">Начать работу</button>
                            </form>
                            <form method="post" action="/orders/${order.id}/reject" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-warning btn-sm" onclick="return confirm('Отказаться от заказа?')">Отказаться</button>
                            </form>
                        </#if>
                        <#if order.status == 'IN_PROGRESS'>
                            <form method="post" action="/orders/${order.id}/complete" style="display: inline;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-success btn-sm">Завершить заказ</button>
                            </form>
                        </#if>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>