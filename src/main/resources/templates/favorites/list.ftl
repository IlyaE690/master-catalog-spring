<#import "../layout/base.ftl" as layout>
<@layout.page title="Избранные мастера">

    <h2>Избранные мастера</h2>

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
                            ${fav.master.rating}
                            <#if fav.note??><br>${fav.note}</#if>
                        </small>
                    </div>
                    <button class="btn btn-outline-danger btn-sm" onclick="removeFromFavorites(${fav.master.id})">
                        Удалить
                    </button>
                </div>
            </#list>
        </div>
    </#if>

</@layout.page>