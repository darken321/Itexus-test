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
INSERT INTO phones (number, user_id) VALUES ('375331234567', 1);
INSERT INTO phones (number, user_id) VALUES ('375441234588', 2);
INSERT INTO phones (number, user_id) VALUES ('375001234599', 3);
INSERT INTO phones (number, user_id) VALUES ('375221234567', 4);
INSERT INTO phones (number, user_id) VALUES ('375991234567', 4);
INSERT INTO phones (number, user_id) VALUES ('375881234567', 4);

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