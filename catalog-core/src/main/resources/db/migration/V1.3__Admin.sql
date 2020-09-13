INSERT INTO accounts (id, uuid, username, password) VALUES (accounts_sq.nextval, 'dc0d73bc-e19e-4c91-b818-192907def7ec', 'admin', '$2a$10$.cmRYarPyPMOGNk9Qj2zI.TjasuXvmxOEgzgD5mSd/HjoumUZzcIS');
INSERT INTO account_roles (account, role) VALUES ((SELECT id FROM accounts WHERE uuid = 'dc0d73bc-e19e-4c91-b818-192907def7ec'), (SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN'));
