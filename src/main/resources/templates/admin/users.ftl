<#import "../layout/base.ftl" as layout>
<@layout.page title="Управление пользователями">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Пользователи</h2>
        <form method="get" class="d-flex gap-2">
            <select name="role" class="form-select w-auto" onchange="this.form.submit()">
                <option value="">Все роли</option>
                <#list roles as r>
                    <option value="${r}" <#if selectedRole?? && selectedRole == r>selected</#if>>${r}</option>
                </#list>
            </select>
            <a href="/admin/users" class="btn btn-secondary">Сбросить</a>
        </form>
    </div>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>

    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя пользователя</th>
                <th>Email</th>
                <th>Имя</th>
                <th>Фамилия</th>
                <th>Роль</th>
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
                    <td>
                        <form method="post" action="/admin/users/${user.id}/role" style="display: inline;">
                            <select name="role" class="form-select form-select-sm" style="width: auto; display: inline-block;">
                                <#list roles as r>
                                    <option value="${r}" <#if user.role == r>selected</#if>>${r}</option>
                                </#list>
                            </select>
                            <button type="submit" class="btn btn-sm btn-outline-primary">✔</button>
                        </form>
                    </td>
                    <td>
                            <span class="badge ${user.enabled?then('bg-success', 'bg-danger')}">
                                ${user.enabled?then('Активен', 'Заблокирован')}
                            </span>
                    </td>
                    <td>
                        <form method="post" action="/admin/users/${user.id}/block" style="display: inline;">
                            <button type="submit" class="btn btn-sm ${user.enabled?then('btn-warning', 'btn-success')}">
                                ${user.enabled?then('Заблокировать', 'Разблокировать')}
                            </button>
                        </form>
                        <form method="post" action="/admin/users/${user.id}/delete" style="display: inline;"
                              onsubmit="return confirm('Удалить пользователя ${user.username}?')">
                            <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

</@layout.page>