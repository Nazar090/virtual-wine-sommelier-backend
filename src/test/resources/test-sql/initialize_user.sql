DELETE FROM users_roles;

DELETE FROM roles;

INSERT INTO roles (id, role)
VALUES (1, 'ADMIN');

INSERT INTO users (email, password, first_name, last_name)
VALUES ('user@example.com', 'password', 'Test', 'User');

INSERT INTO users_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'user@example.com'), 1);
