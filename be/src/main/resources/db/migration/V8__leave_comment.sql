CREATE TABLE LEAVE_COMMENT (id BIGSERIAL PRIMARY KEY,
                           comment TEXT,
                           author varchar(255),
                           request_id bigint CONSTRAINT fk_comment_to_user_leave REFERENCES USER_LEAVE,
                           created_date timestamp NOT NULL,
                           updated_date timestamp NOT NULL,
                           updated_by varchar(255) NOT NULL);
ALTER TABLE LEAVE_COMMENT OWNER TO postgres;
