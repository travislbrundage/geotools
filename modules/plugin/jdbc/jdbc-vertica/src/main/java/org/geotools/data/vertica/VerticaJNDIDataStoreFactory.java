package org.geotools.data.vertica;

import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

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
public class VerticaJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {
    public VerticaJNDIDataStoreFactory() {
        super(new VerticaDataStoreFactory());
    }

    // TODO: May need to override setUpParameters with custom params for Vertica DB
}
