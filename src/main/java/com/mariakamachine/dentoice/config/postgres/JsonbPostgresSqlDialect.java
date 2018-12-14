package com.mariakamachine.dentoice.config.postgres;

import org.hibernate.dialect.PostgreSQL94Dialect;

import static java.sql.Types.JAVA_OBJECT;

public class JsonbPostgresSqlDialect extends PostgreSQL94Dialect {

    public JsonbPostgresSqlDialect() {
        super();
        this.registerColumnType(JAVA_OBJECT, "jsonb");
    }

}
