<#import "../layout/base.ftl" as layout>
<@layout.page title="Избранные мастера">

    <h2>Избранные мастера</h2>

    <#if success??>
        <div class="alert alert-success">${success}</div>
    </#if>
    <#if error??>
        <div class="alert alert-danger">${error}</div>
    </#if>

    <#if favorites?size == 0>
        <p>У вас пока нет избранных мастеров.</p>
        <a href="/masters" class="btn btn-primary">Каталог мастеров</a>
    <#else>
        <div class="list-group">
            <#list favorites as fav>
                <div class="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <strong>
                            <a href="/masters/${fav.master.id}">
                                ${fav.master.firstName!} ${fav.master.lastName!}
                            </a>
                        </strong>
                        <br>
                        <small>
                            Рейтинг: ${fav.master.rating}
                            <#if fav.note??><br>${fav.note}</#if>
                        </small>
                    </div>
                    <form method="post" action="/favorites/remove" style="display: inline;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="masterId" value="${fav.master.id}"/>
                        <button type="submit" class="btn btn-outline-danger btn-sm">Удалить</button>
                    </form>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>