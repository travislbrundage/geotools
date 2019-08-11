package org.geotools.data.vertica;

import org.geotools.jdbc.JDBCBooleanOnlineTest;
import org.geotools.jdbc.JDBCBooleanTestSetup;

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
public class VerticaBooleanOnlineTest extends JDBCBooleanOnlineTest {

    @Override
    protected JDBCBooleanTestSetup createTestSetup() {
        return new VerticaBooleanTestSetup();
    }

}
