INSERT INTO categories (id, name, description, is_deleted) VALUES
    (1, 'Category1', 'Description for Category1', FALSE),
    (2, 'Category2', 'Description for Category2', FALSE),
    (3, 'Category3', 'Description for Category3', FALSE),
    (4, 'Category4', 'Description for Category4', FALSE);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
    (1, 'Book1', 'Author1', 'ISBN-1001', 25.99, 'Description of Book1', 'cover-book1.jpg', FALSE),
    (2, 'Book2', 'Author2', 'ISBN-1002', 30.50, 'Description of Book2', 'cover-book2.jpg', FALSE),
    (3, 'Book3', 'Author3', 'ISBN-1003', 18.75, 'Description of Book3', 'cover-book3.jpg', FALSE),
    (4, 'Book4', 'Author4', 'ISBN-1004', 22.00, 'Description of Book4', 'cover-book4.jpg', FALSE),
    (5, 'Book5', 'Author5', 'ISBN-1005', 27.30, 'Description of Book5', 'cover-book5.jpg', FALSE),
    (6, 'Book6', 'Author6', 'ISBN-1006', 35.60, 'Description of Book6', 'cover-book6.jpg', FALSE),
    (7, 'Book7', 'Author7', 'ISBN-1007', 19.95, 'Description of Book7', 'cover-book7.jpg', FALSE),
    (8, 'Book8', 'Author8', 'ISBN-1008', 40.00, 'Description of Book8', 'cover-book8.jpg', FALSE);

INSERT INTO books_categories (book_id, category_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 1), (5, 2),
    (6, 2), (6, 3),
    (7, 3), (7, 4),
    (8, 1), (8, 4);
