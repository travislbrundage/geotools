package org.geotools.data.vertica;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * HP Vertica database dialect based on basic (non-prepared) statements.
 *
 * @author Travis Brundage, Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaDialectBasic extends BasicSQLDialect {

    VerticaDialect delegate;

    public VerticaDialectBasic(JDBCDataStore dataStore) {
        super( dataStore );
        delegate = new VerticaDialect(dataStore);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
                                       StringBuffer sql) {
        delegate.encodeGeometryEnvelope(tableName, geometryColumn, sql);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
                                           Connection cx) throws SQLException, IOException {
        return delegate.decodeGeometryEnvelope(rs, column, cx);
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql)
            throws IOException {
        delegate.encodeGeometryValue(value, dimension, srid, sql);
    }

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
                                        ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx);
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        return new VerticaFilterToSQL();
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        return delegate.getMapping(columnMetaData, cx);
    }
}