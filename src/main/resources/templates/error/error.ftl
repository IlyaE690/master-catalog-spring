<#import "../layout/base.ftl" as layout>
<@layout.page title="Ошибка ${status}">

    <div class="text-center py-5">
        <h1 class="display-1 text-muted">${status}</h1>
        <h3>Ошибка</h3>
        <p>${message}</p>
        <a href="/" class="btn btn-primary">На главную</a>
    </div>

</@layout.page>