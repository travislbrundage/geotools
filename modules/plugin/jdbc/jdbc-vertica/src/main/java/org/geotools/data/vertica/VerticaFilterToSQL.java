package org.geotools.data.vertica;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.util.logging.Logging;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Intersects;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * HP Vertica SQL Filter
 *
 * @author Travis Brundage. Boundless
 *
 *
 *
 *
 * @source $URL$
 */
public class VerticaFilterToSQL extends FilterToSQL {
    private static final Logger LOGGER = Logging.getLogger(VerticaFilterToSQL.class);

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = super.createFilterCapabilities();
        caps.addType(Intersects.class);

        return caps;
    }
    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        Geometry g = (Geometry) evaluateLiteral(expression, Geometry.class);
        if (g instanceof LinearRing) {
            //WKT does not support linear rings
            g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
        }
        out.write("ST_GeomFromText('"+g.toText()+"', "+currentSRID+")");
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
                                                PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        return visitBinarySpatialOperator(filter, (Expression)property, (Expression)geometry,
                swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1,
                                                Expression e2, Object extraData) {
        return visitBinarySpatialOperator(filter, e1, e2, false, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1,
                                                Expression e2, boolean swapped, Object extraData) {
        try {
            if (filter instanceof Intersects) {
                out.write("ST_Intersects(");
                e1.accept(this, extraData);
                out.write(",");
                e2.accept(this, extraData);
                out.write(")");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }
}