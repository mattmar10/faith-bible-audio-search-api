package com.mattmartin.faithbible.audiosearchapi.db.config;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

/**
 * Wrap default PostgreSQL9Dialect with 'json' type.
 *
 * @author timfulmer
 * https://stackoverflow.com/questions/15974474/mapping-postgresql-json-column-to-hibernate-value-type
 */
public class JSONPostgresSQLDialect extends PostgreSQL9Dialect {

    public JSONPostgresSQLDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}