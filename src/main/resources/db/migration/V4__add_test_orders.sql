INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'test_client', 'client@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Тест', 'Клиент', '+79990001122', 'CUSTOMER', 0.0, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'test_client');

DO $$
DECLARE
client_id BIGINT;
    master_id BIGINT;
    spec_id BIGINT;
BEGIN
SELECT id INTO client_id FROM users WHERE username = 'test_client';
SELECT id INTO master_id FROM users WHERE username = 'plumber_ivan' LIMIT 1;
SELECT id INTO spec_id FROM specializations WHERE name = 'Сантехник' LIMIT 1;

INSERT INTO orders (customer_id, master_id, specialization_id, title, description, address, status, price, created_at, scheduled_date)
SELECT client_id, master_id, spec_id, 'Тестовый заказ 1', 'Проверка работы админ-панели', 'Казань, ул. Тестовая, 1', 'NEW', 2500, NOW(), NOW() + INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Тестовый заказ 1');

INSERT INTO orders (customer_id, master_id, specialization_id, title, description, address, status, price, created_at, scheduled_date, completed_at)
SELECT client_id, master_id, spec_id, 'Тестовый заказ 2', 'Завершенный заказ', 'Казань, ул. Тестовая, 2', 'COMPLETED', 3500, NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Тестовый заказ 2');

INSERT INTO orders (customer_id, specialization_id, title, description, address, status, created_at, scheduled_date)
SELECT client_id, spec_id, 'Тестовый заказ 3', 'Новый заказ без мастера', 'Казань, ул. Тестовая, 3', 'NEW', NOW(), NOW() + INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Тестовый заказ 3');
END $$;