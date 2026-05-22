CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(70) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(60),
    last_name VARCHAR(60),
    phone VARCHAR(20) UNIQUE,
    role VARCHAR(20) NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    bad_weather_price_coefficient DOUBLE PRECISION NOT NULL DEFAULT 1.20,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS specializations (
                                               id BIGSERIAL PRIMARY KEY,
                                               name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    base_price DECIMAL(10,2) NOT NULL,
    weather_dependent BOOLEAN NOT NULL DEFAULT false
    );

CREATE TABLE IF NOT EXISTS user_specializations (
                                                    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    specialization_id BIGINT NOT NULL REFERENCES specializations(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, specialization_id)
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      customer_id BIGINT NOT NULL REFERENCES users(id),
    master_id BIGINT REFERENCES users(id),
    specialization_id BIGINT NOT NULL REFERENCES specializations(id),
    title VARCHAR(150) NOT NULL,
    description TEXT,
    address TEXT NOT NULL,
    image_url TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    price DECIMAL(10,2),
    weather_coefficient DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    created_at TIMESTAMP NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGSERIAL PRIMARY KEY,
                                       order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES users(id),
    target_user_id BIGINT NOT NULL REFERENCES users(id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS notifications (
                                             id BIGSERIAL PRIMARY KEY,
                                             user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read BOOLEAN NOT NULL DEFAULT false,
    related_order_id BIGINT,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS favorite_masters (
                                                id BIGSERIAL PRIMARY KEY,
                                                customer_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    master_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    note VARCHAR(300),
    UNIQUE (customer_id, master_id)
    );

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_master_id ON orders(master_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_scheduled_date ON orders(scheduled_date);
CREATE INDEX idx_notifications_user_id_read ON notifications(user_id, is_read);
CREATE INDEX idx_reviews_target_user_id ON reviews(target_user_id);
