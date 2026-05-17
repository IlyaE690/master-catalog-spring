<#macro page title="ДомБыт">
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${title}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="/css/main.css" rel="stylesheet">
    </head>
    <body>
    <#include "header.ftl">

    <main class="container mt-4 mb-5">
        <#nested>
    </main>

    <#include "footer.ftl">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="/js/validation.js"></script>
    <script src="/js/favorites.js"></script>
    </body>
    </html>
</#macro>