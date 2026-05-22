<#import "../layout/base.ftl" as layout>
<@layout.page title="Заказ #${order.id}">

    <div class="row">
        <div class="col-md-8">
            <#if success??>
                <div class="alert alert-success">${success}</div>
            </#if>
            <#if error??>
                <div class="alert alert-danger">${error}</div>
            </#if>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title mb-0">${order.title}</h3>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Статус:</strong>
                                <span class="badge
                                    <#if order.status == 'NEW'>bg-secondary
                                    <#elseif order.status == 'ASSIGNED'>bg-primary
                                    <#elseif order.status == 'IN_PROGRESS'>bg-info
                                    <#elseif order.status == 'COMPLETED'>bg-success
                                    <#else>bg-danger</#if>">
                                    <#if order.status == 'NEW'>Ожидает мастера
                                    <#elseif order.status == 'ASSIGNED'>Мастер назначен
                                    <#elseif order.status == 'IN_PROGRESS'>В работе
                                    <#elseif order.status == 'COMPLETED'>Завершён
                                    <#else>Отменён</#if>
                                </span>
                            </p>
                            <p><strong>Специализация:</strong> ${order.specialization.name}</p>
                            <p><strong>Адрес:</strong> ${order.address}</p>
                            <p><strong>Описание:</strong> ${order.description!'-'}</p>
                            <p><strong>Дата выполнения:</strong> ${order.formattedScheduledDate!'-'}</p>
                            <p><strong>Создан:</strong> ${order.formattedCreatedAt!'-'}</p>
                            <#if order.completedAt??>
                                <p><strong>Завершён:</strong> ${order.formattedCompletedAt!'-'}</p>
                            </#if>
                        </div>
                        <div class="col-md-6">
                            <#if order.price??>
                                <p><strong>Цена:</strong> <span class="h5">${order.price} ₽</span></p>
                                <#if priceInUsd?? && priceInUsd != 0>
                                    <p><small class="text-muted">≈ ${priceInUsd} USD</small></p>
                                </#if>
                            </#if>

                            <#if order.master??>
                                <p><strong>Мастер:</strong>
                                    <a href="/masters/${order.master.id}">
                                        ${order.master.firstName!} ${order.master.lastName!}
                                    </a>
                                </p>
                                <p><strong>Рейтинг мастера:</strong> ${order.master.rating} ★</p>
                            </#if>
                        </div>
                    </div>

                    <#if order.imageUrl?? && order.imageUrl?has_content>
                        <hr>
                        <p><strong>Фото проблемы:</strong></p>
                        <img src="${order.imageUrl}" class="img-fluid rounded" style="max-width: 100%; max-height: 400px;">
                    </#if>
                </div>
                <div class="card-footer">
                    <div class="d-flex gap-2 flex-wrap">
                        <#if isCustomer>
                            <#if order.status == "NEW" || order.status == "ASSIGNED">
                                <form method="post" action="/orders/${order.id}/cancel"
                                      onsubmit="return confirm('Вы уверены, что хотите отменить заказ?')"
                                      style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-danger">❌ Отменить заказ</button>
                                </form>
                            </#if>

                            <!-- Подтверждение выполнения (если мастер завершил, но заказчик еще не подтвердил) -->
                            <#if order.status == "COMPLETED">
                                <#if order.review??>
                                    <div class="alert alert-success">
                                        <strong>✓ Вы уже оставили отзыв!</strong><br>
                                        Оценка: ${order.review.rating} ★
                                        <#if order.review.comment?? && order.review.comment?has_content>
                                            <br>"${order.review.comment}"
                                        </#if>
                                    </div>
                                <#else>
                                    <a href="/reviews/create/${order.id}" class="btn btn-outline-primary">
                                        Оставить отзыв и оценку
                                    </a>
                                </#if>
                            </#if>
                        </#if>

                        <#if isMaster>
                            <#if order.status == "NEW">
                                <form method="post" action="/orders/${order.id}/accept" style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-success">Принять заказ</button>
                                </form>
                            </#if>

                            <#if order.status == "ASSIGNED">
                                <form method="post" action="/orders/${order.id}/start" style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-primary">Начать работу</button>
                                </form>
                                <form method="post" action="/orders/${order.id}/reject"
                                      onsubmit="return confirm('Отказаться от заказа?')"
                                      style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-warning">Отказаться</button>
                                </form>
                            </#if>

                            <#if order.status == "IN_PROGRESS">
                                <form method="post" action="/orders/${order.id}/complete"
                                      onsubmit="return confirm('Завершить заказ? Клиент сможет оставить отзыв.')"
                                      style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-success">Завершить заказ</button>
                                </form>
                            </#if>
                        </#if>
                    </div>

                    <hr>
                    <div class="mt-3">
                        <a href="<#if isMaster>/orders/assigned<#elseif isCustomer>/orders/my<#else>/</#if>" class="btn btn-secondary">
                            ← Назад
                        </a>
                        <a href="/" class="btn btn-outline-secondary">На главную</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</@layout.page>