<#import "../layout/base.ftl" as layout>
<@layout.page title="Админ-панель">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Панель управления</h2>
    </div>

    <div class="row">
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-primary">
                <div class="card-body">
                    <h5 class="card-title">Пользователи</h5>
                    <p class="card-text display-4">${totalUsers!0}</p>
                    <a href="/admin/users" class="text-white">Управлять →</a>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-success">
                <div class="card-body">
                    <h5 class="card-title">Мастера</h5>
                    <p class="card-text display-4">${totalMasters!0}</p>
                    <a href="/admin/users?role=MASTER" class="text-white">Смотреть →</a>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-info">
                <div class="card-body">
                    <h5 class="card-title">Клиенты</h5>
                    <p class="card-text display-4">${totalCustomers!0}</p>
                    <a href="/admin/users?role=CUSTOMER" class="text-white">Смотреть →</a>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-warning">
                <div class="card-body">
                    <h5 class="card-title">Заказы</h5>
                    <p class="card-text display-4">${totalOrders!0}</p>
                    <a href="/admin/orders" class="text-white">Управлять →</a>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-3">
        <div class="col-md-4 mb-3">
            <div class="card text-white bg-danger">
                <div class="card-body">
                    <h5 class="card-title">Новые заказы</h5>
                    <p class="card-text display-4">${pendingOrders!0}</p>
                    <a href="/admin/orders?status=NEW" class="text-white">Смотреть →</a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card text-white bg-secondary">
                <div class="card-body">
                    <h5 class="card-title">Отзывы</h5>
                    <p class="card-text display-4">${totalReviews!0}</p>
                    <a href="/admin/reviews" class="text-white">Модерировать →</a>
                </div>
            </div>
        </div>
    </div>

</@layout.page>