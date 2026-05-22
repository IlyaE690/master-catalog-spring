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
                        <input type="text" class="form-control" id="username" name="username" value="${username!}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <input type="email" class="form-control" id="email" name="email" value="${email!}" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Пароль *</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="phone" class="form-label">Телефон</label>
                        <input type="text" class="form-control" id="phone" name="phone" value="${phone!}" placeholder="+7XXXXXXXXXX">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName" class="form-label">Имя</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" value="${firstName!}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName" class="form-label">Фамилия</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" value="${lastName!}">
                    </div>
                </div>

                <div class="mb-3">
                    <label for="role" class="form-label">Регистрация как *</label>
                    <select class="form-select" id="role" name="role" required>
                        <option value="CUSTOMER" <#if role?? && role == "CUSTOMER">selected</#if>>Потребитель</option>
                        <option value="MASTER" <#if role?? && role == "MASTER">selected</#if>>Мастер</option>
                    </select>
                </div>

                <div class="mb-3" id="specializationsBlock" style="display: none;">
                    <label class="form-label">Выберите специализации *</label>
                    <div class="row">
                        <#list specializations as spec>
                            <div class="col-md-6 mb-2">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="specializationIds" value="${spec.id}" id="spec_${spec.id}">
                                    <label class="form-check-label" for="spec_${spec.id}">
                                        ${spec.name} (${spec.basePrice} ₽)
                                    </label>
                                </div>
                            </div>
                        </#list>
                    </div>
                    <div id="specializationsError" class="text-danger" style="display: none;">Выберите хотя бы одну специализацию</div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
            </form>

            <p class="mt-3 text-center">
                Уже есть аккаунт? <a href="/login">Войти</a>
            </p>
        </div>
    </div>

    <script>
        var roleSelect = document.getElementById('role');
        var specializationsBlock = document.getElementById('specializationsBlock');

        function toggleSpecializations() {
            if (roleSelect.value === 'MASTER') {
                specializationsBlock.style.display = 'block';
            } else {
                specializationsBlock.style.display = 'none';
            }
        }

        roleSelect.addEventListener('change', toggleSpecializations);

        toggleSpecializations();

        document.getElementById('registerForm').addEventListener('submit', function(e) {
            if (roleSelect.value === 'MASTER') {
                var checkedBoxes = document.querySelectorAll('input[name="specializationIds"]:checked');
                if (checkedBoxes.length === 0) {
                    e.preventDefault();
                    document.getElementById('specializationsError').style.display = 'block';
                }
            }
        });
    </script>

</@layout.page>