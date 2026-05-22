<#import "../layout/base.ftl" as layout>
<@layout.page title="Вход">

    <div class="row justify-content-center">
        <div class="col-md-5">
            <h2 class="mb-4">Вход</h2>

            <#if error??>
                <div class="alert alert-danger">${error}</div>
            </#if>
            <#if registered??>
                <div class="alert alert-success">Регистрация успешна! Войдите в аккаунт.</div>
            </#if>

            <form method="post" action="/login">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="mb-3">
                    <label for="username" class="form-label">Логин</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Пароль</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Войти</button>
            </form>

            <p class="mt-3 text-center">
                Нет аккаунта? <a href="/register">Зарегистрироваться</a>
            </p>
        </div>
    </div>

</@layout.page>