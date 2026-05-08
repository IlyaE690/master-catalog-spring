<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="/">ДомБыт</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/masters">Мастера</a>
                </li>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <#assign auth = Session.SPRING_SECURITY_CONTEXT.authentication>
                    <#if auth.authorities?seq_contains("ROLE_CUSTOMER")>
                        <li class="nav-item">
                            <a class="nav-link" href="/orders/new">Создать заказ</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/orders/my">Мои заказы</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/favorites">Избранное</a>
                        </li>
                    </#if>
                    <#if auth.authorities?seq_contains("ROLE_MASTER")>
                        <li class="nav-item">
                            <a class="nav-link" href="/orders/available">Доступные заказы</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/orders/assigned">Мои заказы</a>
                        </li>
                    </#if>
                    <#if auth.authorities?seq_contains("ROLE_ADMIN")>
                        <li class="nav-item">
                            <a class="nav-link" href="/admin">Админка</a>
                        </li>
                    </#if>
                </#if>
            </ul>
            <ul class="navbar-nav">
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <li class="nav-item">
                        <a class="nav-link" href="/notifications">
                            Уведомления
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/logout">Выйти</a>
                    </li>
                <#else>
                    <li class="nav-item">
                        <a class="nav-link" href="/login">Войти</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/register">Регистрация</a>
                    </li>
                </#if>
            </ul>
        </div>
    </div>
</nav>