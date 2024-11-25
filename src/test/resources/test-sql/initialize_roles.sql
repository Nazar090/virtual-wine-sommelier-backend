DELETE FROM users_roles;

DELETE FROM roles;

INSERT INTO roles (id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');

INSERT INTO users (email, password, first_name, last_name)
VALUES ('testuser@example.com', 'password', 'Test', 'User');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);;