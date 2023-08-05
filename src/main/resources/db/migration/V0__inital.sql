CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE user_tb (
	user_id int8 PRIMARY KEY,
	user_cpf varchar(255) UNIQUE NOT NULL,
	date_sign timestamp NULL,
	user_email varchar(255) UNIQUE NOT NULL,
	is_user_active bool NULL,
	user_name varchar(255) NULL,
	"password" varchar(255) NULL,
	serase_score int4 NULL
);

CREATE TABLE loan_tb (
	loan_id uuid DEFAULT uuid_generate_v4 (),
	approved bool NULL,
	is_payed bool NULL,
	loan_date_due date NULL,
	loan_date_signed date NULL,
	loan_time_frame varchar(255) NULL,
	loan_value numeric(19, 2) NULL,
	value_already_payed numeric(19, 2) NULL,
	user_id int8 NOT NULL,
	PRIMARY KEY (loan_id),
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_tb(user_id)
);

CREATE TABLE loan_payments_tb (
	payment_id uuid DEFAULT uuid_generate_v4 (),
	is_payed bool NULL,
	loan_date_payed date NULL,
	loan_payment_supposed_day date NULL,
	loan_value float8 NULL,
	loan_id uuid NOT NULL,
	PRIMARY KEY (payment_id),
	CONSTRAINT fk_loan FOREIGN KEY (loan_id) REFERENCES loan_tb(loan_id)
);

CREATE TABLE log_tb (
	log_id uuid DEFAULT uuid_generate_v4 (),
	message text NULL,
	request text NULL,
	response text NULL,
	log_date date NULL,
	PRIMARY KEY (log_id)
);

CREATE TABLE access_token (
    id int8 PRIMARY KEY,
    access_token_value varchar(255) NULL,
    active bool NULL,
    PRIMARY KEY (id)
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_tb(user_id)
);

CREATE INDEX token_idx
ON access_token (access_token_value);