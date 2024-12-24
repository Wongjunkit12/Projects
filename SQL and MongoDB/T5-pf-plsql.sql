--*****PLEASE ENTER YOUR DETAILS BELOW*****
--T5-pf-plsql.sql

--Student ID: 32882424
--Student Name: Bryan Jun Kit Wong

/* Comments for your marker:


*/


--(a)
--Write your trigger statement,
--finish it with a slash(/) followed by a blank line
CREATE OR REPLACE TRIGGER CHECK_VISIT_SERVICE_COST
BEFORE INSERT OR UPDATE OR DELETE ON visit_service
FOR EACH ROW
DECLARE
    standard_cost NUMBER(6, 2);
    allowed_var NUMBER(6, 2);
BEGIN
    -- Get the standard cost of the service
    SELECT 
        service_std_cost INTO standard_cost
    FROM 
        service
    WHERE 
        service_code = :new.service_code;

    -- Calculate the allowed variance (10% of the standard cost)
    allowed_var := standard_cost * 0.10;

    -- Check if the new service cost is within the allowed range
    IF inserting OR updating THEN
        IF :new.visit_service_linecost < (standard_cost - allowed_var)
        OR :new.visit_service_linecost > (standard_cost + allowed_var)
            THEN raise_application_error(-20001, 'The service cost must be within 10% of the standard cost.');
        END IF;
    END IF;
END;
/

-- Write Test Harness for (a)
-- initial data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Failed Test trigger - Insert service cost 20% more than standard (> 10%)
BEGIN
    -- Inser the test data with the new visit_id.
    INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
        100, 
        'S010', 
        (
            SELECT
                service_std_cost * 1.20
            FROM
                service
            WHERE
                upper(service_code) = upper('S010')
        )
    );
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Failed Test trigger - Insert service cost 15% less than standard (< 10%)
BEGIN
    INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
        100, 
        'S010', 
        (
            SELECT
                service_std_cost * 0.85
            FROM
                service
            WHERE
                upper(service_code) = upper('S010')
        )
    );
END;
/

-- After test data
SELECT
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Insert service cost 5% more than standard (within 10%)
BEGIN
    INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
        100, 
        'S011', 
        (
            SELECT
                service_std_cost * 1.05
            FROM
                service
            WHERE
                upper(service_code) = upper('S011')
        )
    );
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Insert service cost 7% less than standard (within 10%)
BEGIN
    INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
        100, 
        'S012', 
        (
            SELECT
                service_std_cost * 0.93
            FROM
                service
            WHERE
                upper(service_code) = upper('S012')
        )
    );
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Insert standard service cost (within 10%)
BEGIN
    INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
        100, 
        'S013', 
        (
            SELECT
                service_std_cost
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    );
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Failed Test trigger - Update service cost 15% more than standard (> 10%)
BEGIN
    UPDATE visit_service
    SET
        visit_service_linecost = (
            SELECT
                service_std_cost * 1.15
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    WHERE
        visit_id = 100
        AND upper(service_code) = upper('S013');
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Failed Test trigger - Update service cost 12% less than standard (< 10%)
BEGIN
    UPDATE visit_service
    SET
        visit_service_linecost = (
            SELECT
                service_std_cost * 0.88
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    WHERE
        visit_id = 100
        AND upper(service_code) = upper('S013');
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Update service cost 2% more than standard (within 10%)
BEGIN
    UPDATE visit_service
    SET
        visit_service_linecost = (
            SELECT
                service_std_cost * 1.02
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    WHERE
        visit_id = 100
        AND upper(service_code) = upper('S013');
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Update service cost 10% less than standard (within 10%)
BEGIN
    UPDATE visit_service
    SET
        visit_service_linecost = (
            SELECT
                service_std_cost * 0.90
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    WHERE
        visit_id = 100
        AND upper(service_code) = upper('S013');
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Test trigger - Update service cost to standard (within 10%)
BEGIN
    UPDATE visit_service
    SET
        visit_service_linecost = (
            SELECT
                service_std_cost
            FROM
                service
            WHERE
                upper(service_code) = upper('S013')
        )
    WHERE
        visit_id = 100
        AND upper(service_code) = upper('S013');
END;
/

-- After test data
SELECT 
    * 
FROM 
    visit_service
ORDER BY
    visit_id DESC,
    service_code DESC;

-- Close the transaction
ROLLBACK;
-- End of Test Harness

--(b)
-- Complete the procedure below
CREATE OR REPLACE PROCEDURE prc_followup_visit (
    p_prevvisit_id IN NUMBER,
    p_newvisit_datetime IN DATE,
    p_newvisit_length IN NUMBER,
    p_output OUT VARCHAR2
) IS
    v_prevanimal_id     NUMBER(5);
    v_prevvet_id        NUMBER(4);
    v_prevclinic_id     NUMBER(2);
    v_prev_datetime     DATE;
    v_newvisit_id       NUMBER(5);
    v_num_rows          NUMBER;
BEGIN
    -- Check if p_prevvisit_id corresponds to an existing visit_id within the visit table.
    SELECT 
        COUNT(*) 
    INTO 
        v_num_rows 
    FROM 
        visit 
    WHERE 
        visit_id = p_prevvisit_id;
    
    IF v_num_rows = 0 THEN
        p_output := 'Invalid visit id: Previous visit ID does not exist in the table.';
    ELSE
        -- Check if p_prevvisit_id visit has been completed already
        SELECT 
            COUNT(*) 
        INTO 
            v_num_rows
        FROM 
            visit
        WHERE 
            visit_id = p_prevvisit_id
            AND visit_weight IS NOT NULL
            AND visit_total_cost IS NOT NULL;
        IF v_num_rows = 0 THEN
            p_output := 'Invalid visit id: Previous visit ID has not been completed yet.';
        ELSE
            -- Retrieve details from the previous visit
            SELECT 
                animal_id, 
                vet_id, 
                clinic_id,
                visit_date_time
            INTO 
                v_prevanimal_id, 
                v_prevvet_id, 
                v_prevclinic_id,
                v_prev_datetime
            FROM 
                visit
            WHERE 
                visit_id = p_prevvisit_id;

            -- Check if p_newvisit_datetime date is after the previous visit date
            IF p_newvisit_datetime <= v_prev_datetime THEN
                p_output := 'Invalid visit_datetime: New visit datetime must be after the previous visit datetime.';
            ELSE
                -- Generate new visit ID using the sequence
                v_newvisit_id := visit_seq.NEXTVAL;

                -- Insert the follow-up visit
                INSERT INTO visit (visit_id, visit_date_time, visit_length, visit_notes, visit_weight, visit_total_cost, animal_id, vet_id, clinic_id, from_visit_id) VALUES (
                    v_newvisit_id, 
                    p_newvisit_datetime, 
                    p_newvisit_length, 
                    NULL,
                    NULL,
                    NULL,
                    v_prevanimal_id, 
                    v_prevvet_id, 
                    v_prevclinic_id, 
                    p_prevvisit_id
                );

                -- Insert the service for the follow-up visit
                INSERT INTO visit_service (visit_id, service_code, visit_service_linecost) VALUES (
                    v_newvisit_id, 
                    (
                        SELECT
                            service_code
                        FROM
                            service
                        WHERE
                            upper(service_desc) = upper('General Consultation')
                    ),
                    NULL
                );
                p_output := 'Sucess. The follow-up visit has been booked and recorded.';
            END IF;        
        END IF;
    END IF;   
END;
/

-- Write Test Harness for (b)
-- initial data
SELECT 
    visit_id,
    to_char (
        visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS visit_date,
    visit_length,
    animal_id,
    vet_id,
    clinic_id,
    from_visit_id
FROM 
    visit
ORDER BY
    visit_id DESC;

SELECT 
    *
FROM 
    visit_service
ORDER BY
    visit_id DESC, 
    service_code DESC;

-- Execute the procedure - Previous visit_id does not exist in the visit table.
DECLARE
    output VARCHAR2(200);
BEGIN
    prc_followup_visit(110, to_date('22/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'), 30, output);
    dbms_output.put_line(output);
END;
/

-- After test data
SELECT 
    visit_id,
    to_char (
        visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS visit_date,
    visit_length,
    animal_id,
    vet_id,
    clinic_id,
    from_visit_id
FROM 
    visit
ORDER BY
    visit_id DESC;

SELECT 
    *
FROM 
    visit_service
ORDER BY
    visit_id DESC, 
    service_code DESC;

-- Execute the procedure - Previous visit_id is incomplete
DECLARE
    output VARCHAR2(200);
BEGIN
    prc_followup_visit(8, to_date('22/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'), 30, output);
    dbms_output.put_line(output);
END;
/

-- After test data
SELECT 
    visit_id,
    to_char (
        visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS visit_date,
    visit_length,
    animal_id,
    vet_id,
    clinic_id,
    from_visit_id
FROM 
    visit
ORDER BY
    visit_id DESC;

SELECT 
    *
FROM
    visit_service
ORDER BY
    visit_id DESC, 
    service_code DESC;

-- Execute the procedure - New datetime is before the previous visit datetime.
DECLARE
    output VARCHAR2(200);
BEGIN
    prc_followup_visit(100, to_date('10/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'), 30, output);
    dbms_output.put_line(output);
END;
/

-- After test data
SELECT 
    visit_id,
    to_char (
        visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS visit_date,
    visit_length,
    animal_id,
    vet_id,
    clinic_id,
    from_visit_id
FROM 
    visit
ORDER BY
    visit_id DESC;

SELECT 
    *
FROM
    visit_service
ORDER BY
    visit_id DESC, 
    service_code DESC;

-- Execute the procedure - Successful insert
DECLARE
    output VARCHAR2(200);
BEGIN
    prc_followup_visit(100, to_date('22/05/2024 02:00:00 PM', 'DD/MM/YYYY HH:MI:SS AM'), 30, output);
    dbms_output.put_line(output);
END;
/

-- After test data
SELECT 
    visit_id,
    to_char (
        visit_date_time, 'DD/MM/YYYY HH:MI:SS AM'
    ) AS visit_date,
    visit_length,
    animal_id,
    vet_id,
    clinic_id,
    from_visit_id
FROM 
    visit
ORDER BY
    visit_id DESC;

SELECT 
    *
FROM
    visit_service
ORDER BY
    visit_id DESC, 
    service_code DESC;

-- Close the transaction
ROLLBACK;
-- End of Test Harness