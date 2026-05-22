<#import "../layout/base.ftl" as layout>
<@layout.page title="Каталог мастеров">

    <h2>Каталог мастеров</h2>

    <form method="get" action="/masters" class="row g-3 mb-4">
        <div class="col-md-4">
            <select name="specializationId" class="form-select">
                <option value="">Все специализации</option>
                <#list specializations as spec>
                    <option value="${spec.id}"
                            <#if selectedSpecializationId?? && selectedSpecializationId?string == spec.id?string>selected</#if>>
                        ${spec.name}
                    </option>
                </#list>
            </select>
        </div>
        <div class="col-md-3">
            <select name="minRating" class="form-select">
                <option value="">Любой рейтинг</option>
                <option value="4.0" <#if minRating?? && minRating?string == "4.0">selected</#if>>От 4.0</option>
                <option value="4.5" <#if minRating?? && minRating?string == "4.5">selected</#if>>От 4.5</option>
                <option value="4.8" <#if minRating?? && minRating?string == "4.8">selected</#if>>От 4.8</option>
            </select>
        </div>
        <div class="col-md-3">
            <input type="text" name="query" class="form-control" placeholder="Поиск по имени"
                   value="${query!}">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">Найти</button>
        </div>
    </form>

    <#if masters?size == 0>
        <p>Мастера не найдены.</p>
    <#else>
        <div class="row">
            <#list masters as master>
                <div class="col-md-6 mb-3">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">
                                <a href="/masters/${master.id}">
                                    ${master.firstName!} ${master.lastName!}
                                </a>
                            </h5>
                            <p class="card-text">
                                Рейтинг: ${master.rating}
                                <br>
                                <#list master.specializations as spec>
                                    <span class="badge bg-secondary">${spec.name}</span>
                                </#list>
                            </p>
                            <a href="/masters/${master.id}" class="btn btn-outline-primary btn-sm">Подробнее</a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>