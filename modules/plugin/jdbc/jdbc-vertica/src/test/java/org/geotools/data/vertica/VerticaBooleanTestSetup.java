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
        // TODO: Can AUTO_INCREMENT be combined in this way
        // or do we need to add it to column afterwards?
        run( "CREATE TABLE b (id INT AUTO_INCREMENT PRIMARY KEY, boolProperty BOOLEAN)");
        run( "INSERT INTO b (boolProperty) VALUES (false)");
        run( "INSERT INTO b (boolProperty) VALUES (true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run( "DROP TABLE b");
    }
}
