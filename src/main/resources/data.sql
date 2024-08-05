-- Вставка ролей
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('MODERATOR');
INSERT INTO roles (name) VALUES ('USER');

-- Вставка пользователей
INSERT INTO users (first_name, last_name, email) VALUES ('Иван','Admin','admin@example.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Дмитрий', 'Модератор', 'moderator@example.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Ник', 'Иванов', 'nick.doe@example.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Майк', 'Петров', 'mike.doe@example.com');

-- Вставка телефонов
INSERT INTO user_phones (user_id, phone_number) VALUES (1, 375331234567);
INSERT INTO user_phones (user_id, phone_number) VALUES (2, 375441234588);
INSERT INTO user_phones (user_id, phone_number) VALUES (3, 375001234599);
INSERT INTO user_phones (user_id, phone_number) VALUES (4, 375221234567);
INSERT INTO user_phones (user_id, phone_number) VALUES (4, 375991234567);
INSERT INTO user_phones (user_id, phone_number) VALUES (4, 375881234567);


-- Вставка связей между пользователями и ролями
-- Админ
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- Админ
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2); -- Модератор
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3); -- Юзер
-- Модератор
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- Модератор
-- Пользователи
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- Ник
INSERT INTO user_roles (user_id, role_id) VALUES (4, 3); -- Майк