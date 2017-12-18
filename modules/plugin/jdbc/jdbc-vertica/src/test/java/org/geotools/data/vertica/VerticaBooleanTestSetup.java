package org.geotools.data.vertica;

import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCDataStore;

/**
 * Test harness for HP Vertica database.
 *
 * @author Travis Brundage. Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaBooleanTestSetup extends JDBCBooleanTestSetup {

    protected VerticaBooleanTestSetup() {
        super(new VerticaTestSetup());

    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }
    @Override
    protected void createBooleanTable() throws Exception {
        run( "CREATE TABLE b (id INT PRIMARY KEY, boolProperty BOOLEAN)");
        run( "INSERT INTO b (id, boolProperty) VALUES (1, false)");
        run( "INSERT INTO b (id, boolProperty) VALUES (2, true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE b");
    }
}