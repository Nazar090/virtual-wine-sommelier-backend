-- Clear the data
DELETE FROM users_roles;

DELETE FROM order_items;
ALTER SEQUENCE order_items_id_seq RESTART WITH 1;

DELETE FROM orders;
ALTER SEQUENCE orders_id_seq RESTART WITH 1;

DELETE FROM cart_items;
ALTER SEQUENCE cart_items_id_seq RESTART WITH 1;

DELETE FROM wines;
ALTER SEQUENCE wines_id_seq RESTART WITH 1;

DELETE FROM shopping_carts;
ALTER SEQUENCE shopping_carts_id_seq RESTART WITH 1;

DELETE FROM roles;
ALTER SEQUENCE roles_id_seq RESTART WITH 1;

DELETE FROM users;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

