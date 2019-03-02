CREATE TABLE monthlies(
  id          BIGINT       NOT NULL PRIMARY KEY,
  dentist     BIGINT       NOT NULL REFERENCES dentists (id),
  description VARCHAR(255) NOT NULL,
  date        DATE         NOT NULL,
  invoices    BIGINT[]     NOT NULL,
  skonto      BIGINT       NOT NULL,
  total       DECIMAL      NOT NULL
);
