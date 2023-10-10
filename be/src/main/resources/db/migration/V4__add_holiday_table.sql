CREATE TABLE HOLIDAY (id BIGSERIAL PRIMARY KEY,
                   name varchar(255) NOT NULL,
                   description varchar(255),
                   from_date timestamp NOT NULL ,
                   to_date timestamp NOT NULL ,
                   created_date timestamp NOT NULL,
                   updated_date timestamp, updated_by varchar(255));
ALTER TABLE HOLIDAY OWNER TO postgres;
--
INSERT INTO holiday (name, description, from_date, to_date, created_date, updated_date, updated_by)
VALUES ('2/9',null, now(), now(), now(), now(), null);