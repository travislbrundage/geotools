package org.geotools.data.vertica;

import org.geotools.jdbc.JDBCDataStoreFactory;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * DataStoreFactory Test for HP Vertica database.
 *
 * @author Travis Brundage. Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaDataStoreFactoryTest extends TestCase {
    VerticaDataStoreFactory factory;

    protected void setUp() throws Exception {
        factory = new VerticaDataStoreFactory();
    }

    public void testCanProcess() throws Exception {
        HashMap params = new HashMap();
        assertFalse(factory.canProcess(params));

        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "geotools");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "vertica");

        params.put(JDBCDataStoreFactory.HOST.key, "localhost");
        params.put(JDBCDataStoreFactory.PORT.key, "5433");
        params.put(JDBCDataStoreFactory.USER.key, "verticauser");
        assertTrue(factory.canProcess(params));
    }
}
