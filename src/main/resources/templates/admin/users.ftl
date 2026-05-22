<#import "../layout/base.ftl" as layout>
<@layout.page title="Управление пользователями">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Пользователи</h2>
    </div>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <div class="row mb-3">
        <div class="col-md-4">
            <form method="get" class="d-flex gap-2">
                <input type="text" name="search" class="form-control" placeholder="Поиск по имени, логину" value="${search!}">
                <button type="submit" class="btn btn-primary">Найти</button>
            </form>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Логин</th>
                <th>Email</th>
                <th>Имя</th>
                <th>Фамилия</th>
                <th>Телефон</th>
                <th>Роль</th>
                <th>Рейтинг</th>
                <th>Статус</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <#list users as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.firstName!'-'}</td>
                    <td>${user.lastName!'-'}</td>
                    <td>${user.phone!'-'}</td>
                    <td>
                        <form method="post" action="/admin/users/${user.id}/role" style="display: inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <select name="role" class="form-select form-select-sm" style="width: auto; display: inline-block;">
                                <#list roles as r>
                                    <option value="${r}" <#if user.role == r>selected</#if>>${r}</option>
                                </#list>
                            </select>
                            <button type="submit" class="btn btn-sm btn-outline-primary">✔</button>
                        </form>
                    </td>
                    <td>${user.rating} ★</td>
                    <td>
                        <span class="badge ${user.enabled?then('bg-success', 'bg-danger')}">
                            ${user.enabled?then('Активен', 'Заблокирован')}
                        </span>
                    </td>
                    <td>
                        <form method="post" action="/admin/users/${user.id}/block" style="display: inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-sm ${user.enabled?then('btn-warning', 'btn-success')}">
                                ${user.enabled?then('Заблокировать', 'Разблокировать')}
                            </button>
                        </form>
                        <form method="post" action="/admin/users/${user.id}/delete" style="display: inline;"
                              onsubmit="return confirm('Удалить пользователя ${user.username}?')">
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