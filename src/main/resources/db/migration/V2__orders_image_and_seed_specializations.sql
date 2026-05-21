ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS image_url TEXT;

INSERT INTO specializations (name, description, base_price, weather_dependent)
SELECT 'Сантехник', 'Устранение протечек, ремонт смесителей, установка сантехники', 2500, false
    WHERE NOT EXISTS (SELECT 1 FROM specializations WHERE name = 'Сантехник');

INSERT INTO specializations (name, description, base_price, weather_dependent)
SELECT 'Электрик', 'Ремонт проводки, розеток, автоматов, диагностика коротких замыканий', 3000, false
    WHERE NOT EXISTS (SELECT 1 FROM specializations WHERE name = 'Электрик');

INSERT INTO specializations (name, description, base_price, weather_dependent)
SELECT 'Отделочник', 'Покраска, штукатурка, укладка плитки, косметический ремонт', 3500, false
    WHERE NOT EXISTS (SELECT 1 FROM specializations WHERE name = 'Отделочник');

INSERT INTO specializations (name, description, base_price, weather_dependent)
SELECT 'Кровельщик', 'Ремонт крыши, герметизация стыков, замена элементов кровли', 5000, true
    WHERE NOT EXISTS (SELECT 1 FROM specializations WHERE name = 'Кровельщик');

INSERT INTO specializations (name, description, base_price, weather_dependent)
SELECT 'Фасадчик', 'Ремонт фасада, утепление, герметизация швов', 4500, true
    WHERE NOT EXISTS (SELECT 1 FROM specializations WHERE name = 'Фасадчик');
