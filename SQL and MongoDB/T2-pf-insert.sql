/*****PLEASE ENTER YOUR DETAILS BELOW*****/
--T2-pf-insert.sql

--Student ID: 32882424
--Student Name: Bryan Jun Kit Wong

/* Comments for your marker:




*/

--------------------------------------
--INSERT INTO visit
--------------------------------------
INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (1, TO_DATE('02/04/2024 10:30 AM', 'DD/MM/YYYY HH:MI AM'), 45, 'Annual Checkup', 12.2, 311.99, 1, 1001, 1, NULL);   -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (2, TO_DATE('03/04/2024 11:00 AM', 'DD/MM/YYYY HH:MI AM'), 60, 'Skin irritation', 4.8, 323.00, 3, 1002, 2, NULL); -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (3, TO_DATE('12/06/2024 01:00 PM', 'DD/MM/YYYY HH:MI PM'), 60, NULL, NULL, NULL, 12, 1005, 2, NULL);  -- Incomplete visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (4, TO_DATE('17/04/2024 03:30 PM', 'DD/MM/YYYY HH:MI PM'), 30, 'Skin irritation - Follow-up', 4.8, 80.00, 3, 1002, 4, 2);  -- Follow-up visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (5, TO_DATE('22/04/2024 09:00 AM', 'DD/MM/YYYY HH:MI AM'), 50, 'Ear infection', 3.1, 225.00, 7, 1001, 1, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (6, TO_DATE('30/04/2024 02:00 PM', 'DD/MM/YYYY HH:MI PM'), 40, 'Emergency visit - Vomiting', 8.2, 409.00, 10, 1003, 5, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (7, TO_DATE('08/05/2024 02:30 PM', 'DD/MM/YYYY HH:MI PM'), 45, 'General Consultation - Lethargy', 15.4, 86.00, 4, 1001, 1, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (8, TO_DATE('12/06/2024 10:00 AM', 'DD/MM/YYYY HH:MI AM'), 90, NULL, NULL, NULL, 4, 1011, 4, 7);  -- Incomplete visit (Future Follow-up)

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (9, TO_DATE('10/05/2024 11:30 AM', 'DD/MM/YYYY HH:MI AM'), 30, 'Ear infection - Follow-up', 3.1, 70.00, 7, 1001, 1, 5);  -- Follow-up visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (10, TO_DATE('20/05/2024 01:30 PM', 'DD/MM/YYYY HH:MI PM'), 90, 'Behaviour Problems', 6.1, 235.00, 11, 1007, 5, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (11, TO_DATE('20/05/2024 03:30 PM', 'DD/MM/YYYY HH:MI PM'), 60, 'Toothache', 2.8, 110.00, 6, 1004, 3, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (12, TO_DATE('29/05/2024 02:00 PM', 'DD/MM/YYYY HH:MI PM'), 40, 'Microchipping', 5.1, 120.00, 9, 1009, 5, NULL);  -- Completed visit

INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id)
    VALUES (13, TO_DATE('01/06/2024 10:00 AM', 'DD/MM/YYYY HH:MI AM'), 90, 'Behavioural Problems - Follow-up', 6.1, 315.00, 11, 1007, 5, 10);  -- Follow-up visit

--------------------------------------
--INSERT INTO visit_service
--------------------------------------
INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (1, 'S011', 90.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (1, 'S002', 50.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (2, 'S008', 40.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (2, 'S009', 85.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (3, 'S002', NULL);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (4, 'S009', 80.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (5, 'S010', 75.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (6, 'S012', 100.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (6, 'S004', 150.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (6, 'S013', 110.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (7, 'S001', 50.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (8, 'S016', NULL);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (9, 'S010', 70.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (10, 'S001', 55.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (10, 'S020', 100.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (11, 'S006', 80.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (12, 'S003', 70.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (13, 'S001', 55.00);

INSERT INTO visit_service (visit_id, service_code, visit_service_linecost)
    VALUES (13, 'S020', 100.00);

--------------------------------------
--INSERT INTO visit_drug
--------------------------------------
INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (1, 102, 'Single dose', NULL, 1, 99.99);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (1, 111, 'One tablet', 'Once daily', 60, 72.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (2, 108, '10 mg/kg', 'Once every month', 3, 135.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (2, 109, '1.1 mg/kg', 'Once daily', 14, 63.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (5, 110, '5 mg/kg', 'Once daily', 5, 150.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (6, 112, '7 ml/kj', NULL, 1, 15.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (6, 113, '30 mil', NULL, 2, 6.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (6, 114, '0.01mg/kg', 'Single dose', 2, 28.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (7, 111, 'One tablet', 'Once daily', 30, 36.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (10, 120, 'Behavioural tricks', NULL, 1, 80.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (11, 106, '50 ml diluted', NULL, 3, 30.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (12, 103, 'Single chip', NULL, 1, 50.00);

INSERT INTO visit_drug (visit_id, drug_id, visit_drug_dose, visit_drug_frequency, visit_drug_qtysupplied, visit_drug_linecost)
    VALUES (13, 120, 'Single dose', NULL, 2, 160.00);

-- Commit the transaction.
COMMIT;