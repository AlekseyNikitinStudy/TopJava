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

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2021-06-20 10:00:00', 'breakfast-400', 400, 100000),
       ('2021-06-20 14:00:00', 'dinner-300', 300, 100000),
       ('2021-06-20 18:00:00', 'supper-1000', 1000, 100000),
       ('2021-06-21 00:00:00', 'nightmare-2000', 2000, 100000),
       ('2021-06-21 10:00:00', 'breakfast-200', 200, 100000),
       ('2021-06-21 15:00:00', 'dinner-500', 500, 100000),
       ('2021-06-22 00:00:00', 'midnight light eater-200', 200, 100000),
       ('2021-06-22 13:00:00', 'breakfast-400', 400, 100000),
       ('2021-06-22 16:00:00', 'dinner-500', 500, 100000),
       ('2021-06-22 23:59:59', 'hungry games-100', 100, 100000),
       ('2021-06-21 10:00:00', 'breakfast', 200, 100001),
       ('2021-06-21 15:00:00', 'dinner', 300, 100001);
