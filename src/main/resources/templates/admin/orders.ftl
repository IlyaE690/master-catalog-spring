<#import "../layout/base.ftl" as layout>
<@layout.page title="Управление заказами">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Заказы</h2>
    </div>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>

    <div class="row mb-3">
        <div class="col-md-3">
            <form method="get" class="d-flex gap-2">
                <select name="status" class="form-select" onchange="this.form.submit()">
                    <option value="">Все статусы</option>
                    <#list statuses as s>
                        <option value="${s}" <#if selectedStatus?? && selectedStatus == s>selected</#if>>${s}</option>
                    </#list>
                </select>
                <a href="/admin/orders" class="btn btn-secondary">Сбросить</a>
            </form>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Заголовок</th>
                    <th>Клиент</th>
                    <th>Мастер</th>
                    <th>Специализация</th>
                    <th>Статус</th>
                    <th>Цена</th>
                    <th>Дата</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
            <#list orders as order>
                <tr>
                    <td>${order.id}</td>
                    <td>${order.title}</td>
                    <td>${order.customer.username}</#if></td>
                    <td><#if order.master??>${order.master.username}<#else>-</#if></td>
                    <td>${order.specialization.name}</td>
                    <td>
                        <form method="post" action="/admin/orders/${order.id}/status" style="display: inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <select name="status" class="form-select form-select-sm" style="width: auto; display: inline-block;">
                                <#list statuses as s>
                                    <option value="${s}" <#if order.status == s>selected</#if>>${s}</option>
                                </#list>
                            </select>
                            <button type="submit" class="btn btn-sm btn-outline-primary">Изменить</button>
                        </form>
                      </td>
                    <td>${order.price?then(order.price, '-')}</td>
                    <td>${order.formattedScheduledDate!''}</td>
                    <td>
                        <form method="post" action="/admin/orders/${order.id}/delete" style="display: inline;"
                              onsubmit="return confirm('Удалить заказ #${order.id}?')">
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