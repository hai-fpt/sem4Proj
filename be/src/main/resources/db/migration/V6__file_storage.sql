CREATE TABLE FILE_STORAGE (id BIGSERIAL PRIMARY KEY,
                        name varchar(255) NOT NULL,
                        path varchar(255) NOT NULL,
                        request_id bigint CONSTRAINT fk_file_storage_to_user_leave REFERENCES USER_LEAVE,
                        created_date timestamp NOT NULL,
                        updated_date timestamp NOT NULL,
                        updated_by varchar(255) NOT NULL);
ALTER TABLE FILE_STORAGE OWNER TO postgres;
