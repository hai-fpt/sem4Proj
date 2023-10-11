CREATE TABLE CONFIGURATION (id BIGSERIAL PRIMARY KEY,
                            milestone_year   int,
                            limit_attachment int,
                            created_date     timestamp    NOT NULL,
                            updated_date     timestamp    NOT NULL,
                            updated_by       varchar(255) NOT NULL
);
ALTER TABLE CONFIGURATION OWNER TO postgres;
INSERT INTO CONFIGURATION (id, milestone_year, limit_attachment, created_date, updated_date, updated_by)
                    VALUES (1, 3, 10, now(), now(), 'ducan@mz.co.kr');
