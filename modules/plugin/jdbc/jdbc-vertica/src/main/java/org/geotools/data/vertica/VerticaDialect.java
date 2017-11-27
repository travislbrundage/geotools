package org.geotools.data.vertica;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTWriter;
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
 * Delegate for {@link VerticaDialectBasic}
 * which implements the common part of the api.
 *
 * @author Travis Brundage, Boundless
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

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
                                       StringBuffer sql) {
        sql.append("ST_Envelope(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append(")");
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
                                           Connection cx) throws SQLException, IOException {
        byte[] wkb = rs.getBytes(column);

        try {
            Geometry geometry = new WKBReader().read(wkb);
            return geometry.getEnvelopeInternal();
        } catch (ParseException e) {
            String msg = "Error decoding wkb for envelope";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql)
            throws IOException {
        if (value != null) {
            sql.append("ST_GeomFromText('");
            sql.append(new WKTWriter().write(value));
            sql.append("', ").append(srid).append(")");
        }
        else {
            sql.append("NULL");
        }
    }

    // TODO: Fix
    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
                                        ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        byte[] bytes = rs.getBytes(column);
        if ( bytes == null ) {
            return null;
        }

        try {
            return new WKBReader(factory).read(bytes);
        } catch (ParseException e) {
            String msg = "Error decoding wkb";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
}
