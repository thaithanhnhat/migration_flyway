INSERT INTO redis_demo.product (id, name, category)
SELECT 4, 'dienthoai5', 'dien tu2'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM redis_demo.product WHERE id = 4
);
