UPDATE leave
SET affects_days_off = CASE WHEN name = 'Unpaid Leave' THEN true ELSE false END;
