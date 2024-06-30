CREATE TABLE IF NOT EXISTS customer
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS transaction
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    customer_id VARCHAR(100) NOT NULL,
    transaction_date DATE,
    bill_amount INT,
    reward_points INT,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY(customer_id) REFERENCES customer(id)
);