
CREATE TABLE user_tb (
	user_id int8 GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	user_cpf varchar(255) UNIQUE NOT NULL,
	date_sign timestamp NULL,
	user_email varchar(255) UNIQUE NOT NULL,
	is_user_active bool NULL,
	user_name varchar(255) NULL,
	"password" varchar(255) NULL,
	serase_score int4 NULL
);

CREATE TABLE loan_tb (
	loan_id varchar(255) PRIMARY KEY NOT NULL,
	approved bool NULL,
	is_payed bool NULL,
	loan_date_due date NULL,
	loan_date_signed date NULL,
	loan_time_frame varchar(255) NULL,
	loan_value numeric(19, 2) NULL,
	value_already_payed numeric(19, 2) NULL,
	user_id int8 NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_tb(user_id)
);

CREATE TABLE loan_payments_tb (
	payment_id varchar(255) PRIMARY KEY NOT NULL,
	is_payed bool NULL,
	loan_date_payed date NULL,
	loan_payment_supposed_day date NULL,
	loan_value float8 NULL,
	loan_id varchar(255) NOT NULL,
	CONSTRAINT fk_loan FOREIGN KEY (loan_id) REFERENCES public.loan_tb(loan_id)
);