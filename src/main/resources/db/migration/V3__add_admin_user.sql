INSERT INTO users (username, email, password, first_name, last_name, phone, role, rating, enabled, created_at)
SELECT 'admin', 'admin@household.ru',
       '$2a$10$NkX5k9qYqZqYqZqYqZqYqZqYqZqYqZqYqZqYqZqYqZqYqZqYqZqYqZ',
       'Админ', 'Системный', '+70000000001', 'ADMIN', 0.0, true, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');