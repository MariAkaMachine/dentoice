CREATE TABLE dentists (
  id         BIGINT       NOT NULL CONSTRAINT pk_dentist PRIMARY KEY,
  title      VARCHAR(255),
  first_name VARCHAR(255),
  last_name  VARCHAR(255) NOT NULL,
  street     VARCHAR(255) NOT NULL,
  zip        VARCHAR(255) NOT NULL,
  city       VARCHAR(255) NOT NULL,
  phone      VARCHAR(255),
  fax        VARCHAR(255),
  email      VARCHAR(255)
);
