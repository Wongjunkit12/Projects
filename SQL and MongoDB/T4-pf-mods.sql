/*****PLEASE ENTER YOUR DETAILS BELOW*****/
--T4-pf-mods.sql

--Student ID: 32882424
--Student Name: Bryan Jun Kit Wong


/* Comments for your marker:




*/

/*(a)*/
-- Add a new column to track the number of non standard cost services done.
ALTER TABLE service ADD non_standard_cost_count NUMBER(3) DEFAULT 0;

COMMENT ON COLUMN service.non_standard_cost_count IS
    'Count on the number of times non-standard cost is applied.';

-- Update the table to have the new column by counting the number of non standard costs.
UPDATE service s
SET s.non_standard_cost_count = (
    SELECT 
        COUNT(*)
    FROM 
        visit_service vs
    WHERE 
        vs.service_code = s.service_code 
        AND vs.visit_service_linecost != s.service_std_cost
);

-- Commit the transaction.
COMMIT;

-- Display the structure of the service table.
DESC service;

-- Select statement to show the data changes in the 'service' table
SELECT 
    service_code, 
    service_desc, 
    non_standard_cost_count 
FROM 
    service
ORDER BY
    service_code;

/*(b)*/
-- Drop payment tables before creation.
DROP TABLE payment_method CASCADE CONSTRAINTS PURGE;

DROP TABLE visit_payment CASCADE CONSTRAINTS PURGE;

-- Create the payment_method table to track payment methods.
CREATE TABLE payment_method (
    payment_method_id       NUMBER(5) NOT NULL,
    payment_method_name     VARCHAR2(20) NOT NULL
);

COMMENT ON COLUMN payment_method.payment_method_id IS
    'Payment method identifier';

COMMENT ON COLUMN payment_method.payment_method_name IS
    'Name of the payment method';

ALTER TABLE payment_method ADD CONSTRAINT payment_method_pk PRIMARY KEY ( payment_method_id );

-- Create the visit_payment table to track payment of visits.
CREATE TABLE visit_payment (
    visit_id                NUMBER(5) NOT NULL,
    payment_method_id       NUMBER(5) NOT NULL,
    visit_payment_date      DATE NOT NULL,
    visit_payment_amount    NUMBER(6, 2) NOT NULL
);

COMMENT ON COLUMN visit_payment.visit_id IS
    'Identifier for visit';

COMMENT ON COLUMN visit_payment.payment_method_id IS
    'Payment method identifier';

COMMENT ON COLUMN visit_payment.visit_payment_date IS
    'Date the payment was made';

COMMENT ON COLUMN visit_payment.visit_payment_date IS
    'Amount paid in the payment';

ALTER TABLE visit_payment ADD CONSTRAINT visit_payment_pk PRIMARY KEY ( visit_id, payment_method_id );

ALTER TABLE visit_payment
    ADD CONSTRAINT visit_payment_fk FOREIGN KEY ( visit_id ) 
        REFERENCES visit ( visit_id );

ALTER TABLE visit_payment
    ADD CONSTRAINT payment_method_fk FOREIGN KEY ( payment_method_id ) 
        REFERENCES payment_method ( payment_method_id );

-- Add the 3 payment methods into the payment_method table.
INSERT INTO payment_method (payment_method_id, payment_method_name) VALUES (1, 'Historical');

INSERT INTO payment_method (payment_method_id, payment_method_name) VALUES (2, 'Card');

INSERT INTO payment_method (payment_method_id, payment_method_name) VALUES (3, 'Cash');

-- Change all past payments as 'Historical' method.
INSERT INTO visit_payment (visit_id, payment_method_id, visit_payment_date, visit_payment_amount)
SELECT 
    visit_id,
    (
        SELECT
            payment_method_id
        FROM
            payment_method
        WHERE
            upper(payment_method_name) = upper('Historical')
    ),
    visit_date_time,
    visit_total_cost
FROM 
    visit
WHERE
    visit_total_cost IS NOT NULL;

-- Commit the transaction.
COMMIT;

-- Display the structure of the payment_method table.
DESC payment_method;

-- Display the structure of the visit_payment table.
DESC visit_payment;

-- Show the payment_method and visit_payment table.
SELECT 
    * 
FROM 
    payment_method 
ORDER BY 
    payment_method_id;

SELECT
    visit_id,
    payment_method_id,
    to_char (
        visit_payment_date, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS payment_date,
    visit_payment_amount
FROM 
    visit_payment
ORDER BY
    visit_id;