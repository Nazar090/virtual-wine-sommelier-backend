INSERT INTO users (email, password, first_name, last_name)
VALUES ('testuser@example.com', 'password', 'Test', 'User');

INSERT INTO shopping_carts (user_id)
VALUES (1);

INSERT INTO orders (user_id, status, total, order_date, address_id)
VALUES (1, 'PROCESSING', 100.05, NOW(), 1),
       (1, 'COMPLETED', 40.00, NOW(), 1);

INSERT INTO wines (name, type, color, strength, country, grape, price, description)
VALUES
    ('Wine 1', 'Dry', 'Dark Red', '13%', 'France', 'Merlot', 20.00, 'A smooth and rich red wine.'),
    ('Wine 2', 'Semi-Dry', 'Light Yellow', '12%', 'Italy', 'Chardonnay', 30.00, 'A refreshing white wine with crisp flavors.');

INSERT INTO order_items (order_id, wine_id, quantity, price)
VALUES
    (1, 1, 2, 20.00),
    (2, 2, 3, 30.00);
