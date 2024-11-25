INSERT INTO users (email, password, first_name, last_name)
VALUES ('testuser@example.com', 'password', 'Test', 'User');

INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1);

INSERT INTO wines (name, type, color, strength, country, grape, price, description)
VALUES
    ('Sample Wine 1', 'Red', 'Dark Red', '13%', 'France', 'Merlot', 20.00, 'A smooth and rich red wine.'),
    ('Sample Wine 2', 'White', 'Light Yellow', '12%', 'Italy', 'Chardonnay', 30.00, 'A refreshing white wine with crisp flavors.');

INSERT INTO cart_items (wine_id, shopping_cart_id, quantity)
VALUES ((SELECT id FROM wines WHERE name = 'Sample Wine 1'), 1, 2),
       ((SELECT id FROM wines WHERE name = 'Sample Wine 2'), 1, 1);
