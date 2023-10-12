CREATE TABLE AVATAR (id BIGSERIAL PRIMARY KEY,
                           name varchar(255) NOT NULL,
                           path varchar(255) NOT NULL,
                           user_id bigint CONSTRAINT fk_avatar_to_user REFERENCES USERS,
                           created_date timestamp NOT NULL,
                           updated_date timestamp NOT NULL,
                           updated_by varchar(255) NOT NULL);
ALTER TABLE AVATAR OWNER TO postgres;
