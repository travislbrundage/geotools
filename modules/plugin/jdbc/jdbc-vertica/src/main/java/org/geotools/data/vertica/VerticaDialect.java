package org.geotools.data.vertica;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;

import org.geotools.util.logging.Logging;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logging.getLogger(VerticaDialect.class);

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
        // TODO: This might require similar logic to decodeGeometryValue
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

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
                                        ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        // get the index and table name for WKB generation
        int index = rs.findColumn(column);
        ResultSetMetaData rsmd = rs.getMetaData();
        String tableName = rsmd.getTableName(index);
        LOGGER.log(Level.WARNING, "tableName just to be sure: " + tableName);

        // Vertica requires a call to ST_AsBinary on the column
        // to convert spatial data to WKB
        Statement statement = null;
        ResultSet result = null;

        try {
            String sqlStatement = "SELECT ST_AsBinary(" + column + ")" //
                    + " FROM " + tableName;

            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);
            if (result.next()) {
                byte[] bytes = result.getBytes(1);
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
        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        return null;
    }

    String lookupGeometryType(ResultSet columnMetaData, Connection cx) throws SQLException {
        // grab the information we need to proceed
        String tableName = columnMetaData.getString("TABLE_NAME");
        String columnName = columnMetaData.getString("COLUMN_NAME");

        Statement statement = null;
        ResultSet result = null;

        try {
            String sqlStatement = "SELECT ST_GeometryType(" + columnName + ")" //
                    + " FROM " + tableName;

            LOGGER.log(Level.FINE, "Geometry type check; {0} ", sqlStatement);
            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                return result.getString(1);
            }
        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        return null;
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        String typeName = columnMetaData.getString("TYPE_NAME");

        if("geometry".equalsIgnoreCase(typeName) || "geography".equalsIgnoreCase(typeName)) {
            // determine the type held in the geometry column
            String geomType = lookupGeometryType(columnMetaData, cx);
            LOGGER.log(Level.WARNING, "geomType result from lookup: " + geomType);

            if (geomType == "ST_Point") {
                return Point.class;
            } else if(geomType == "ST_MultiPoint") {
                return MultiPoint.class;
            } else if(geomType == "ST_Polygon") {
                return Polygon.class;
            } else if(geomType == "ST_MultiPolygon") {
                return MultiPolygon.class;
            } else if(geomType == "ST_LineString") {
                return LineString.class;
            } else if(geomType == "ST_MultiLineString") {
                return MultiLineString.class;
            } else if(geomType == "ST_GeometryCollection") {
                return GeometryCollection.class;
            } else {
                return Geometry.class;
            }
        } else {
            return super.getMapping(columnMetaData, cx);
        }
    }
}
