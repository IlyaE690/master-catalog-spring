<#import "../layout/base.ftl" as layout>
<@layout.page title="Выбор специализации">

    <div class="row justify-content-center">
        <div class="col-md-8">
            <h2 class="mb-4">Выбор специализации</h2>

            <div class="alert alert-info">
                <strong>Здравствуйте, ${master.firstName!} ${master.lastName!}!</strong><br>
                Вы зарегистрированы как мастер. Пожалуйста, выберите специализации, которые вы предоставляете.
            </div>

            <#if error??>
                <div class="alert alert-danger">${error}</div>
            </#if>

            <#if success??>
                <div class="alert alert-success">${success}</div>
            </#if>

            <form method="post" action="/master/specializations">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Мои специализации</h5>
                        <div class="row">
                            <#list allSpecializations as spec>
                                <div class="col-md-6 mb-2">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="specializationIds" value="${spec.id}" id="spec_${spec.id}"
                                               <#if userSpecializations?seq_contains(spec)>checked</#if>>
                                        <label class="form-check-label" for="spec_${spec.id}">
                                            <strong>${spec.name}</strong><br>
                                            <small class="text-muted">${spec.description!''} — от ${spec.basePrice} ₽</small>
                                        </label>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary mt-3">Сохранить специализации</button>
                <a href="/" class="btn btn-secondary mt-3">На главную</a>
            </form>
        </div>
    </div>

</@layout.page>