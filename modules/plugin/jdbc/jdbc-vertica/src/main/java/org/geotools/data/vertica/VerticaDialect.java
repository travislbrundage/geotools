package org.geotools.data.vertica;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * HP Vertica database dialect
 *
 * @author Travis Brundage. Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaDialect extends SQLDialect {

    public VerticaDialect( JDBCDataStore dataStore ) {
        super( dataStore );
    }

    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        //TODO
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
                                           Connection cx) throws SQLException, IOException {

        //TODO
        return (Envelope) rs.getObject(column);
    }

    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column,
                                        GeometryFactory factory, Connection cx ) throws IOException, SQLException {
        // TODO
        return null;
    }
}
