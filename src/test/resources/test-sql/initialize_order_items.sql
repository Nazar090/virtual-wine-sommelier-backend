INSERT INTO users (email, password, first_name, last_name)
VALUES ('testuser@example.com', 'password', 'Test', 'User');

INSERT INTO orders (user_id, status, total, order_date, address_id)
VALUES (1, 'PROCESSING', 100.00, NOW(), 1);

INSERT INTO wines (name, type, color, strength, country, grape, price, description)
VALUES
    ('Sample Wine 1', 'Red', 'Dark Red', '13%', 'France', 'Merlot', 20.00, 'A smooth and rich red wine.'),
    ('Sample Wine 2', 'White', 'Light Yellow', '12%', 'Italy', 'Chardonnay', 30.00, 'A refreshing white wine with crisp flavors.');

INSERT INTO order_items (order_id, wine_id, quantity, price)
VALUES
    (1, (SELECT id FROM wines WHERE name = 'Sample Wine 1'), 2, 20.00),
    (1, (SELECT id FROM wines WHERE name = 'Sample Wine 2'), 1, 30.00);
