CREATE TABLE materials (
  position       VARCHAR(10)      NOT NULL CONSTRAINT pk_material PRIMARY KEY,
  name           VARCHAR(255)     NOT NULL,
  price_per_unit DOUBLE PRECISION NOT NULL,
  is_metal       BOOLEAN          NOT NULL
);
