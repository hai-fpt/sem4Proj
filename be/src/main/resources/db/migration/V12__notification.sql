CREATE TABLE NOTIFICATION (id             BIGSERIAL PRIMARY KEY,
                           type           boolean,
                           sender_id      bigint,
                           sender_name    varchar(255),
                           sender_email   varchar(255),
                           receiver_id    bigint,
                           receiver_name  varchar(255),
                           receiver_email varchar(255),
                           status         varchar(255),--PENDING/CANCEL/APPROVED/REJECT/FINAL-APPROVED/FINAL-REJECT
                           is_watched     boolean,
                           leave_from     timestamp NOT NULL,
                           leave_to       timestamp NOT NULL,
                           created_date   timestamp NOT NULL,
                           watched_date   timestamp NOT NULL
);
ALTER TABLE NOTIFICATION OWNER TO postgres;
