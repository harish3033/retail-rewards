INSERT INTO customer (id, name) VALUES ('73af1c90-525e-4114-a9fd-fb13200866e3', 'customer-1');
INSERT INTO customer (id, name) VALUES ('41383b72-e07f-42ad-bfd0-e2e281c37437', 'customer-2');
COMMIT;

INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('ee0e1ae2-9ceb-43b9-ada5-1b9dfe568fb1', '73af1c90-525e-4114-a9fd-fb13200866e3', '2024-06-15', 50, 0);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('dc277e98-bb03-4d18-a7ef-22fe6df950f3', '73af1c90-525e-4114-a9fd-fb13200866e3', '2024-06-16', 60, 10);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('1cac3f46-580b-4c38-a189-2f87c0ff8983', '73af1c90-525e-4114-a9fd-fb13200866e3', '2024-06-17', 70, 20);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('ce9bfabe-2437-4a96-a06d-57da8f5149c3', '73af1c90-525e-4114-a9fd-fb13200866e3', '2024-06-18', 80, 30);

INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('b89893c0-922d-4a5b-887d-94e8224ed439', '41383b72-e07f-42ad-bfd0-e2e281c37437', '2024-06-19', 90, 40);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('3768b3a5-7b19-42f5-8cf4-dd7fe8bc1ca3', '41383b72-e07f-42ad-bfd0-e2e281c37437', '2024-06-20', 100, 50);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('3eb0a5cc-1a2c-40bb-9e6f-ec208dd8125f', '41383b72-e07f-42ad-bfd0-e2e281c37437', '2024-06-21', 110, 70);
INSERT INTO transaction(id, customer_id, transaction_date, bill_amount, reward_points) VALUES ('4849f971-a3b8-4fba-885f-74494010193c', '41383b72-e07f-42ad-bfd0-e2e281c37437', '2024-06-22', 120, 90);
COMMIT;