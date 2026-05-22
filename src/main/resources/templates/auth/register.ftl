<#import "../layout/base.ftl" as layout>
<@layout.page title="Регистрация">

    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="mb-4">Регистрация</h2>

            <#if error??>
                <div class="alert alert-danger">${error}</div>
            </#if>

            <form method="post" action="/register" id="registerForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="username" class="form-label">Логин *</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                        <div class="invalid-feedback" id="usernameFeedback"></div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                        <div class="invalid-feedback" id="emailFeedback"></div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Пароль *</label>
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
                    <label for="role" class="form-label">Регистрация как *</label>
                    <select class="form-select" id="role" name="role" required>
                        <option value="CUSTOMER">Потребитель</option>
                        <option value="MASTER">Мастер</option>
                    </select>
                </div>

                <div class="mb-3" id="specializationsBlock" style="display: none;">
                    <label for="specializations" class="form-label">Выберите специализации *</label>
                    <select class="form-select" id="specializations" name="specializations" multiple size="5">
                        <#list specializations as spec>
                            <option value="${spec.id}">${spec.name}</option>
                        </#list>
                    </select>
                    <small class="text-muted">Зажмите Ctrl (или Cmd на Mac) для выбора нескольких</small>
                    <div id="specializationsError" class="invalid-feedback" style="display: none;">Выберите хотя бы одну специализацию</div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
            </form>

            <p class="mt-3 text-center">
                Уже есть аккаунт? <a href="/login">Войти</a>
            </p>
        </div>
    </div>

    <script>
        document.getElementById('role').addEventListener('change', function() {
            const specializationsBlock = document.getElementById('specializationsBlock');
            if (this.value === 'MASTER') {
                specializationsBlock.style.display = 'block';
            } else {
                specializationsBlock.style.display = 'none';
            }
        });

        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const role = document.getElementById('role').value;
            if (role === 'MASTER') {
                const specializations = document.getElementById('specializations');
                const selectedCount = Array.from(specializations.selectedOptions).length;
                if (selectedCount === 0) {
                    e.preventDefault();
                    document.getElementById('specializationsError').style.display = 'block';
                    alert('Для регистрации как мастер выберите хотя бы одну специализацию');
                }
            }
        });
    </script>

</@layout.page>