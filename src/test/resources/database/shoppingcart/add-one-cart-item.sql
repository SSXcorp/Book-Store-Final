INSERT INTO cart_items (id, book_id, quantity, shopping_cart_id)
VALUES (1, 1, 1, (SELECT id FROM shopping_carts WHERE user_id = 1));
