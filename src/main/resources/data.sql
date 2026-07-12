INSERT INTO courses (code, name, description, credit_units)
SELECT 'CSC101',
       'Introduction to Computer Science',
       'Basic computer science concepts',
       3
WHERE NOT EXISTS (
    SELECT 1 FROM courses WHERE code = 'CSC101'
);

INSERT INTO courses (code, name, description, credit_units)
SELECT 'MAT101',
       'Introduction to Mathematics',
       'Fundamental mathematics concepts',
       3
WHERE NOT EXISTS (
    SELECT 1 FROM courses WHERE code = 'MAT101'
);