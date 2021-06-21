DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id)
VALUES ('2021-06-21 10:00:00', 'breakfast', 400, 100000),
       ('2021-06-21 14:00:00', 'dinner', 300, 100000),
       ('2021-06-22 8:00:00', 'breakfast-2', 450, 100000),
       ('2021-06-21 10:00:00', 'breakfast', 200, 100001),
       ('2021-06-21 15:00:00', 'dinner', 300, 100001);
