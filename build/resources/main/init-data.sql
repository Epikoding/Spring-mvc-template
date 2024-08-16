INSERT INTO authority (id, created_at, deleted, modified_at, role)
VALUES (1, NOW(), 0, NULL, 'ADMIN'),
       (2, NOW(), 0, NULL, 'BLOCKED'),
       (3, NOW(), 0, NULL, 'NORMAL'),
       (4, NOW(), 0, NULL, 'TEMPORARY')
    ON DUPLICATE KEY UPDATE
                         created_at = VALUES(created_at),
                         deleted = VALUES(deleted),
                         modified_at = VALUES(modified_at),
                         role = VALUES(role);