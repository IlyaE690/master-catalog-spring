-- Initial incremental migration for new semester requirements
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS bad_weather_price_coefficient DOUBLE PRECISION NOT NULL DEFAULT 1.20;
