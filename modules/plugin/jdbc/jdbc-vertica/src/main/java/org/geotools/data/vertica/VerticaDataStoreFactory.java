package org.geotools.data.vertica;

import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

import java.util.Collections;
import java.util.Map;

/**
 * DataStoreFactory for HP Vertica database.
 *
 * @author Travis Brundage. Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true,"vertica",
            Collections.singletonMap(Parameter.LEVEL, "program"));
    /** Default port number for MYSQL */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 5433);

    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new VerticaDialect(dataStore);
    }

    public String getDisplayName() { return "Vertica"; }

    protected String getDriverClassName() { return "com.vertica.jdbc.Driver"; }

    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    public String getDescription() {
        return "HP Vertica Database";
    }

    @Override
    protected String getValidationQuery() {
        return "select version()";
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(PORT.key, PORT);
    }
}
