CREATE TABLE efforts (
  position       VARCHAR(10)      NOT NULL CONSTRAINT pk_effort PRIMARY KEY,
  name           VARCHAR(255)     NOT NULL,
  price_per_unit DOUBLE PRECISION NOT NULL
);
