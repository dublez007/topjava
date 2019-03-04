DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
(100000, '2019-02-11 09:00:00', 'Завтрак', 800),
(100001, '2019-02-12 14:00:00', 'Обед', 1200),
(100000, '2019-02-11 15:00:00', 'Обед', 1400),
(100000, '2019-02-12 08:00:00', 'Завтрак', 800),
(100001, '2019-02-12 14:30:00', 'Обед', 800);

