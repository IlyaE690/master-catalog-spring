INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'plumber_ivan', 'ivan@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Иван', 'Петров', '+79001112233', 'MASTER', 4.9, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'plumber_ivan');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'plumber_alexey', 'alexey@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Алексей', 'Сидоров', '+79002223344', 'MASTER', 4.2, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'plumber_alexey');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'electric_dmitry', 'dmitry@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Дмитрий', 'Кузнецов', '+79003334455', 'MASTER', 4.8, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'electric_dmitry');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'electric_sergey', 'sergey@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Сергей', 'Михайлов', '+79004445566', 'MASTER', 4.7, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'electric_sergey');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'finisher_andrey', 'andrey@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Андрей', 'Соколов', '+79005556677', 'MASTER', 4.6, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'finisher_andrey');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'painter_vladimir', 'vladimir@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Владимир', 'Новиков', '+79006667788', 'MASTER', 4.4, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'painter_vladimir');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'roofer_roman', 'roman@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Роман', 'Морозов', '+79007778899', 'MASTER', 4.9, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'roofer_roman');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'facade_artem', 'artem@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Артём', 'Волков', '+79008889900', 'MASTER', 4.8, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'facade_artem');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'cleaner_elena', 'elena@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Елена', 'Смирнова', '+79009990011', 'MASTER', 4.5, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'cleaner_elena');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'cleaner_maxim', 'maxim@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Максим', 'Фёдоров', '+79001001122', 'MASTER', 4.3, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'cleaner_maxim');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'furniture_konstantin', 'konstantin@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Константин', 'Егоров', '+79002002233', 'MASTER', 4.6, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'furniture_konstantin');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'repair_mikhail', 'mikhail@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Михаил', 'Зайцев', '+79003003344', 'MASTER', 4.7, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'repair_mikhail');

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'repair_nikolay', 'nikolay@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Николай', 'Борисов', '+79004004455', 'MASTER', 4.8, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'repair_nikolay');

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 1 FROM users u WHERE u.username = 'plumber_ivan'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 1 FROM users u WHERE u.username = 'plumber_alexey'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 2 FROM users u WHERE u.username = 'electric_dmitry'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 2 FROM users u WHERE u.username = 'electric_sergey'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 3 FROM users u WHERE u.username = 'finisher_andrey'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 3 FROM users u WHERE u.username = 'painter_vladimir'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 4 FROM users u WHERE u.username = 'roofer_roman'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 5 FROM users u WHERE u.username = 'facade_artem'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 6 FROM users u WHERE u.username = 'cleaner_elena'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 6 FROM users u WHERE u.username = 'cleaner_maxim'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 7 FROM users u WHERE u.username = 'furniture_konstantin'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 8 FROM users u WHERE u.username = 'repair_mikhail'
    ON CONFLICT DO NOTHING;

INSERT INTO user_specializations (user_id, specialization_id)
SELECT u.id, 8 FROM users u WHERE u.username = 'repair_nikolay'
    ON CONFLICT DO NOTHING;

INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'test_customer', 'customer@example.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Петр', 'Тестов', '+79005005566', 'CUSTOMER', 0.0, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'test_customer');

DO $$
DECLARE
var_customer_id BIGINT;
    var_plumber_id BIGINT;
    var_electric_id BIGINT;
    var_finisher_id BIGINT;
BEGIN
SELECT id INTO var_customer_id FROM users WHERE username = 'test_customer';
SELECT id INTO var_plumber_id FROM users WHERE username = 'plumber_ivan';
SELECT id INTO var_electric_id FROM users WHERE username = 'electric_dmitry';
SELECT id INTO var_finisher_id FROM users WHERE username = 'finisher_andrey';

INSERT INTO orders (customer_id, specialization_id, title, description, address, status, created_at, scheduled_date)
SELECT var_customer_id, 1, 'Починить кран на кухне', 'Капает вода из-под ручки крана', 'г. Казань, ул. Баумана, 10, кв. 42', 'NEW', NOW(), NOW() + INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Починить кран на кухне');

INSERT INTO orders (customer_id, master_id, specialization_id, title, description, address, status, price, created_at, scheduled_date)
SELECT var_customer_id, var_electric_id, 2, 'Заменить розетку', 'Не работает розетка в спальне', 'г. Казань, ул. Кремлёвская, 5, кв. 15', 'ASSIGNED', 2500, NOW(), NOW() + INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Заменить розетку');

INSERT INTO orders (customer_id, master_id, specialization_id, title, description, address, status, price, created_at, scheduled_date)
SELECT var_customer_id, var_finisher_id, 3, 'Поклеить обои', 'Поклеить обои в зале, площадь 25 кв.м', 'г. Казань, пр. Победы, 20, кв. 8', 'IN_PROGRESS', 5000, NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Поклеить обои');

INSERT INTO orders (customer_id, master_id, specialization_id, title, description, address, status, price, created_at, scheduled_date, completed_at)
SELECT var_customer_id, var_plumber_id, 1, 'Заменить смеситель', 'Установить новый смеситель в ванной', 'г. Казань, ул. Чистопольская, 15, кв. 7', 'COMPLETED', 3500, NOW() - INTERVAL '10 days', NOW() - INTERVAL '8 days', NOW() - INTERVAL '7 days'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE title = 'Заменить смеситель');

INSERT INTO reviews (order_id, author_id, target_user_id, rating, comment, created_at)
SELECT o.id, var_customer_id, var_plumber_id, 5, 'Отличный мастер! Быстро и качественно заменил смеситель. Рекомендую!', NOW() - INTERVAL '6 days'
FROM orders o WHERE o.title = 'Заменить смеситель'
                AND NOT EXISTS (SELECT 1 FROM reviews r JOIN orders o2 ON r.order_id = o2.id WHERE o2.title = 'Заменить смеситель');

END $$;