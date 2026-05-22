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
                            <a class="nav-link" href="/admin">Админ-панель</a>
                        </li>
                    </#if>
                </#if>
            </ul>
            <ul class="navbar-nav">
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <li class="nav-item">
                        <a class="nav-link" href="/notifications">Уведомления</a>
                    </li>
                    <li class="nav-item" id="chooseSpecializationLi" style="display: none;">
                        <a class="nav-link text-warning" href="/master/specializations">⚠️ Выбрать специализацию</a>
                    </li>
                    <li class="nav-item">
                        <form method="post" action="/logout" style="display: inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-link nav-link" style="display: inline; padding: 8px 16px; margin: 0; border: none; background: none; color: rgba(255,255,255,0.85);">
                                Выйти
                            </button>
                        </form>
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

<script>
    <#if Session.SPRING_SECURITY_CONTEXT??>
    <#assign auth = Session.SPRING_SECURITY_CONTEXT.authentication>
    <#if auth.authorities?seq_contains("ROLE_MASTER")>
    fetch('/master/check')
        .then(response => response.json())
        .then(data => {
            if (!data.hasSpecializations) {
                document.getElementById('chooseSpecializationLi').style.display = 'block';
            }
        })
        .catch(err => console.log(err));
    </#if>
    </#if>
</script>