SELECT * FROM customer;
SELECT * FROM transaction;
SELECT * FROM customer c INNER JOIN transaction t on c.id = t.customer_id;