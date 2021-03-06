CREATE TABLE invoices (
  id             BIGINT       NOT NULL CONSTRAINT pk_invoice PRIMARY KEY,
  dentist        BIGINT       NOT NULL REFERENCES dentists (id),
  patient        VARCHAR(255) NOT NULL,
  color          VARCHAR(255),
  description    VARCHAR(255) NOT NULL,
  xml_number     VARCHAR(255) NOT NULL,
  invoice_type   VARCHAR(255) NOT NULL,
  insurance_type VARCHAR(255) NOT NULL,
  date           DATE         NOT NULL,
  mwst           BIGINT       NOT NULL,
  costs          JSONB        NOT NULL,
  brutto         DECIMAL      NOT NULL
);
