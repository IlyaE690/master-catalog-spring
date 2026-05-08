<#import "../layout/base.ftl" as layout>
<@layout.page title="Регистрация">

    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="mb-4">Регистрация</h2>

            <#if error??>
                <div class="alert alert-danger">${error}</div>
            </#if>

            <form method="post" action="/register">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="username" class="form-label">Логин</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="phone" class="form-label">Телефон</label>
                        <input type="text" class="form-control" id="phone" name="phone">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName" class="form-label">Имя</label>
                        <input type="text" class="form-control" id="firstName" name="firstName">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName" class="form-label">Фамилия</label>
                        <input type="text" class="form-control" id="lastName" name="lastName">
                    </div>
                </div>

                <div class="mb-3">
                    <label for="role" class="form-label">Роль</label>
                    <select class="form-select" id="role" name="role" required>
                        <#list roles as role>
                            <option value="${role}">${role}</option>
                        </#list>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
            </form>

            <p class="mt-3 text-center">
                Уже есть аккаунт? <a href="/login">Войти</a>
            </p>
        </div>
    </div>

</@layout.page>