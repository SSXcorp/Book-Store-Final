INSERT IGNORE INTO roles (id, name) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER');

INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES
    (1, 'admin@admin.com', '$2a$10$e1d9a9faksaw2e', 'Admin', 'User', '123 Admin St', FALSE)
    ON DUPLICATE KEY UPDATE email = email;

INSERT IGNORE INTO users_roles (user_id, role_id) VALUES
  (1, 1);

