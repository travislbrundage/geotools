package org.geotools.data.vertica;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

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
public class VerticaTestSetup extends JDBCTestSetup {
    @Override
    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        super.initializeDataSource(ds, db);

        ds.setDefaultTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED );
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new VerticaDataStoreFactory();
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected Properties createExampleFixture() {
        Properties p = new Properties();

        p.put("driver", "com.vertica.jdbc.Driver");
        p.put("url", "jdbc:vertica://localhost/geotools");
        p.put("host", "localhost");
        p.put("port", "5433");
        p.put("user", "geotools");
        p.put("password", "geotools");

        return p;
    }
}
