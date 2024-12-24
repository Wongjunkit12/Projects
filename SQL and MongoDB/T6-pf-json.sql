/*****PLEASE ENTER YOUR DETAILS BELOW*****/
--T6-pf-json.sql

--Student ID: 32882424
--Student Name: Bryan Jun Kit Wong


/* Comments for your marker:




*/

-- PLEASE PLACE REQUIRED SQL SELECT STATEMENT TO GENERATE 
-- THE COLLECTION OF JSON DOCUMENTS HERE
-- ENSURE that your query is formatted and has a semicolon
-- (;) at the end of this answer

SELECT 
    JSON_OBJECT (
        '_id' VALUE c.clinic_id,
        'name' VALUE c.clinic_name,
        'address' VALUE c.clinic_address,
        'phone' VALUE c.clinic_phone,
        'head_vet' VALUE JSON_OBJECT (
            'id' VALUE hv.vet_id,
            'name' VALUE hv.vet_givenname || ' ' || hv.vet_familyname
        ),
        'no_of_vets' VALUE (
            SELECT 
                COUNT(*) 
            FROM 
                vet v 
            WHERE 
                v.clinic_id = c.clinic_id
        ),
        'vets' VALUE JSON_ARRAYAGG (
            JSON_OBJECT (
                'id' VALUE v.vet_id,
                'name' VALUE v.vet_givenname || ' ' || v.vet_familyname,
                'specialisation' VALUE nvl(s.spec_description, 'N/A')
            )
        ORDER BY 
            s.spec_description, 
            v.vet_id
        )
    FORMAT JSON )
    || ','
FROM 
    clinic c
    JOIN vet hv ON c.vet_id = hv.vet_id
    LEFT OUTER JOIN vet v ON c.clinic_id = v.clinic_id
    LEFT OUTER JOIN specialisation s ON v.spec_id = s.spec_id
GROUP BY 
    c.clinic_id, 
    c.clinic_name, 
    c.clinic_address, 
    c.clinic_phone, 
    hv.vet_id, 
    hv.vet_givenname, 
    hv.vet_familyname
ORDER BY
    c.clinic_id;