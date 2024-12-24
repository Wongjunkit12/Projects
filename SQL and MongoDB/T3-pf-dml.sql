/*****PLEASE ENTER YOUR DETAILS BELOW*****/
--T3-pf-dml.sql

--Student ID: 32882424
--Student Name: Bryan Jun Kit Wong

/* Comments for your marker:




*/

/*(a)*/
-- Drop the sequence before creation.
DROP SEQUENCE visit_seq;

-- Create the sequence starting with 100 with an incrementation of 10.
CREATE SEQUENCE visit_seq
    START WITH 100 INCREMENT BY 10;

/*(b)*/
-- Make visit booking for Jack Jones and his pet rabbit Oreo at 19 May 2024 at 2pm for 30 minutes.
-- Book with Dr Anna KOWALSKI at clinic id 3.
INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id) VALUES (
    visit_seq.nextval, 
    to_date('19/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'),
    30,
    NULL, 
    NULL,
    NULL, 
    (
        SELECT
            animal_id
        FROM
            animal
        WHERE
            upper(animal_name) = upper('Oreo')
            AND to_char (
                animal_born, 'YYYY-MM-DD'
            ) = '2018-06-01'
    ), 
    (
        SELECT
            vet_id
        FROM
            vet
        WHERE
            upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    ),
    3,
    NULL
);

-- Book the visit service to be general consultation (service code: S001) initially.
INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
    visit_seq.currval,
    'S001',
    NULL
);

-- Commit the transaction.
COMMIT;

/*(c)*/
-- Update the service to 'ear infection treatment' with the code and price being updated accordingly.
UPDATE visit_service
SET 
    service_code = (
        SELECT
            service_code
        FROM
            service
        WHERE
            upper(service_desc) = upper('ear infection treatment')
    ),
    visit_service_linecost = (
        SELECT
            service_std_cost
        FROM
            service
        WHERE
            upper(service_desc) = upper('ear infection treatment')
    )
WHERE
    visit_id = (
        SELECT
            visit_id
        FROM
            visit v
            JOIN vet vt ON v.vet_id = vt.vet_id
        WHERE
            to_char (
                visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
            ) = '19/05/2024 02:00:00 PM'
            AND upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    );

-- Insert a new visit_drug entry for the prescribed one bottle of Clotrimazole for the visit.
INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost) VALUES (
    (
        SELECT
            visit_id
        FROM
            visit v
            JOIN vet vt ON v.vet_id = vt.vet_id
        WHERE
            to_char (
                visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
            ) = '19/05/2024 02:00:00 PM'
            AND upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    ),
    (
        SELECT
            drug_id
        FROM
            drug
        WHERE
            upper(drug_name) = upper('Clotrimazole')
    ),
    'Apply in ear',
    'Once daily',
    1,
    (
        SELECT
            drug_std_cost * 1
        FROM
            drug
        WHERE
            upper(drug_name) = upper('Clotrimazole')
    )
);

-- Update the visit entry with the new information, ear infection treatment, Oreo weight, and the total cost of the service and 
-- prescribed drug.
UPDATE visit v
SET 
    v.visit_notes = 'ear infection treatment',
    v.visit_weight = 2.5,
    v.visit_total_cost = (
        SELECT
            SUM(vs.visit_service_linecost) + SUM(vd.visit_drug_linecost)
        FROM
            visit_service vs
            LEFT OUTER JOIN visit_drug vd ON vs.visit_id = vd.visit_id
        WHERE
            vs.visit_id = v.visit_id
    )
WHERE
    v.visit_id = (
        SELECT
            visit_id
        FROM
            visit v
            JOIN vet vt ON v.vet_id = vt.vet_id
        WHERE
            to_char (
                visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
            ) = '19/05/2024 02:00:00 PM'
            AND upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    );

-- Book a follow-up visit at 2pm seven days after this visit for another ear infection treatment at the same clinic.
INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id) VALUES (
    visit_seq.NEXTVAL,
    to_date (
        '19/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'
    ) + INTERVAL '7' DAY,
    30,
    NULL,
    NULL,
    NULL,
    (
        SELECT
            animal_id
        FROM
            animal
        WHERE
            upper(animal_name) = upper('Oreo')
            AND to_char (
                animal_born, 'YYYY-MM-DD'
            ) = '2018-06-01'
    ),
    (
        SELECT
            vet_id
        FROM
            vet
        WHERE
            upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    ),
    3,
    (
        SELECT
            visit_id
        FROM
            visit v
            JOIN vet vt ON v.vet_id = vt.vet_id
        WHERE
            to_char (
                visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
            ) = '19/05/2024 02:00:00 PM'
            AND upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    )
);

-- Book the follow-up visit service for another ear infection treatment.
INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
    visit_seq.currval,
    (
        SELECT
            service_code
        FROM
            service
        WHERE
            upper(service_desc) = upper('ear infection treatment')
    ),
    NULL
);

-- Commit the transaction.
COMMIT;

/*(d)*/
-- Remove the booking entry from the visit service table.
DELETE FROM visit_service
WHERE 
    visit_id = (
        SELECT
            visit_id
        FROM
            VISIT
        WHERE
            visit_date_time = to_date('19/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM') + INTERVAL '7' DAY
            AND vet_id = (
                SELECT
                    vet_id
                FROM
                    vet
                WHERE
                    upper(vet_givenname) = upper('Anna')
                    AND upper(vet_familyname) =  upper('KOWALSKI')
            )
    );

-- Remove the booking entry from the visit table.
DELETE FROM visit
WHERE 
    visit_date_time = to_date('19/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM') + INTERVAL '7' DAY
    AND vet_id = (
        SELECT
            vet_id
        FROM
            vet
        WHERE
            upper(vet_givenname) = upper('Anna')
            AND upper(vet_familyname) =  upper('KOWALSKI')
    );

-- Commit the transaction.
COMMIT;