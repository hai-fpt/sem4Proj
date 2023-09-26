CREATE DATABASE IF NOT EXISTS lms;
-- INIT TABLES STRUCTURE FOR LEAVE MANAGEMENT SYSTEM
CREATE TABLE LEAVE (id BIGSERIAL PRIMARY KEY,
                    name varchar(255),
                    description varchar(255),
                    updated_by varchar(255),
                    created_date timestamp NOT NULL,
                    updated_date timestamp,
                    CONSTRAINT leave_name_unique UNIQUE (name));
ALTER TABLE LEAVE OWNER TO postgres;

CREATE TABLE ROLES (id BIGSERIAL PRIMARY KEY,
                    name varchar(255),
                    description varchar(255),
                    updated_by varchar(255),
                    created_date timestamp NOT NULL,
                    updated_date timestamp,
                    CONSTRAINT roles_name_unique UNIQUE (name));
ALTER TABLE ROLES OWNER TO postgres;

CREATE TABLE DEPARTMENT (id BIGSERIAL PRIMARY KEY,
                    name varchar(255),
                    description varchar(255),
                    updated_by varchar(255),
                    created_date timestamp NOT NULL,
                    updated_date timestamp,
                    CONSTRAINT department_name_unique UNIQUE (name));
ALTER TABLE DEPARTMENT OWNER TO postgres;

CREATE TABLE USERS (id BIGSERIAL PRIMARY KEY,
                    email varchar(255) NOT NULL,
                    phone varchar(255),
                    date_of_birth timestamp, department varchar(255),
                    rank varchar(255) NOT NULL,
                    status boolean NOT NULL,
                    skills varchar(255),
                    department_id bigint CONSTRAINT fktwdvvwpei4kv5lm0tsss63x9depart REFERENCES DEPARTMENT,
                    university varchar(255),
                    university_code varchar(255),
                    university_graduate_date timestamp,
                    joined_date timestamp, name varchar(255),
                    resigned_date timestamp,
                    created_date timestamp NOT NULL,
                    updated_date timestamp,
                    updated_by varchar(255),
                    CONSTRAINT email_unique UNIQUE (email));
ALTER TABLE USERS OWNER TO postgres;

CREATE TABLE TEAM (id BIGSERIAL PRIMARY KEY,
                   name varchar(255) NOT NULL,
                   description varchar(255) NOT NULL,
                   manager_id bigint CONSTRAINT fktwdvvwpei4kv5lm0tsss63x9 REFERENCES USERS,
                   created_date timestamp NOT NULL,
                   updated_date timestamp, updated_by varchar(255),
                   CONSTRAINT team_name_unique UNIQUE (name));
ALTER TABLE TEAM OWNER TO postgres;

CREATE TABLE USER_LEAVE (id BIGSERIAL PRIMARY KEY,
                         user_id bigint CONSTRAINT fkth3tftmo56ehkqviwky64bqt REFERENCES USERS,
                         leave_id bigint CONSTRAINT fkr7j15ev089r7uxivvc3esxaxu REFERENCES LEAVE,
                         from_date timestamp,
                         to_date timestamp,
                         inform_to bigint[],
                         status integer,
                         created_date timestamp NOT NULL,
                         updated_date timestamp,
                         updated_by varchar(255));
ALTER TABLE USER_LEAVE OWNER TO postgres;

CREATE TABLE LEAVE_APPROVAL (id BIGSERIAL PRIMARY KEY,
                             user_leave_id bigint CONSTRAINT fke41ugiuo46syautuwlrqordam REFERENCES USER_LEAVE,
                             manager_id varchar(255),
                             status integer,
                             created_date timestamp NOT NULL,
                             updated_by varchar(255),
                             updated_date timestamp);
ALTER TABLE LEAVE_APPROVAL OWNER TO postgres;

CREATE TABLE USER_ROLE (user_id bigint NOT NULL CONSTRAINT fkj345gk1bovqvfame88rcx7yyx REFERENCES USERS,
                        role_id bigint NOT NULL CONSTRAINT fkt7e7djp752sqn6w22i6ocqy6q REFERENCES ROLES,
                        PRIMARY KEY (role_id,
                                     user_id));
ALTER TABLE USER_ROLE OWNER TO postgres;

CREATE TABLE USER_TEAM (id BIGSERIAL PRIMARY KEY,
                        team_id bigint CONSTRAINT fk6d6agqknw564xtsa91d3259wu REFERENCES TEAM,
                        user_id bigint CONSTRAINT fkmodbby1xpn7sf5rmw7f81n0v4 REFERENCES USERS,
                        created_date timestamp NOT NULL,
                        updated_date timestamp,
                        updated_by varchar(255));
ALTER TABLE USER_TEAM OWNER TO postgres;

-- INIT DEFAULT DATA
-- Create admin user
INSERT INTO users (id, created_date, date_of_birth, department, email, joined_date, name, phone, rank, resigned_date,
    skills, status, university, university_code, university_graduate_date, updated_by, updated_date)
VALUES (1, now(), now(), null, 'hainvhth2108031@fpt.edu.vn', now(), 'ADMINISTRATOR','0988228263', 'EMPLOYEE', now(),
    null, true, null, null, null, null, null);

-- Create leave type
INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Annual Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Sick Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Hospitalisation Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Maternity Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Emergency Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Unpaid Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Study/Exam Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Compassionate Leave', now(), null, now(), now());

INSERT INTO leave (name, created_date, description, updated_by, updated_date)
VALUES ('Others', now(), null, now(), now());

-- Create role for user
INSERT INTO roles (id, name, created_date, description, updated_by, updated_date)
VALUES (1, 'ADMIN', now(), null, null, now());

INSERT INTO roles (id, name, created_date, description, updated_by, updated_date)
VALUES (2, 'MANAGER', now(), null, null, now());

INSERT INTO roles (id, name, created_date, description, updated_by, updated_date)
VALUES (3, 'USER', now(), null, null, now());

-- Create default role for ADMIN
INSERT INTO public.user_role (user_id, role_id) VALUES (1, 1);

-- Create default team
INSERT INTO team (name, description, created_date, updated_date, updated_by, manager_id)
VALUES ('Solution Team', 'Solution Team', now(), now(), null, null);

INSERT INTO team (name, description, created_date, updated_date, updated_by, manager_id)
VALUES ('eCommerce Team', 'eCommerce Team', now(), now(), null, null);

INSERT INTO team (name, description, created_date, updated_date, updated_by, manager_id)
VALUES ('Mobile Team', 'Mobile Team', now(), now(), null, null);

-- Create default department
INSERT INTO department (name, description, updated_by, created_date, updated_date)
VALUES ('Dept', 'Dept', null, now(), now())
