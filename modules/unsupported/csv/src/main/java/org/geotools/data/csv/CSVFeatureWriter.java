package org.geotools.data.csv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.csv.parse.CSVIterator;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Point;

public class CSVFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
	
    private SimpleFeatureType featureType;
    
    private CSVFileState csvFileState;
    
    /** Temporary file used to stage output */
    private File temp;
    
    /** Delegate handling reading of original file(?) */
    private CSVIterator iterator;
    
    /** CsvWriter used for temp file output */
    private CsvWriter csvWriter;
    
    /** Flag indicating we have reached the end of the file */
    private boolean appending = false;
    
    /** Row count used to generate FeatureId when appending */
    int nextRow = 0;
    
    /** Current feature available for modification. May be null if feature removed */
    private SimpleFeature currentFeature;

    public CSVFeatureWriter(CSVFileState csvFileState, CSVStrategy csvStrategy)
    		throws IOException {
        this(csvFileState, csvStrategy, Query.ALL);
    }
    
    public CSVFeatureWriter(CSVFileState csvFileState, CSVStrategy csvStrategy, Query query)
    		throws IOException {
    	this.featureType = csvStrategy.getFeatureType();
    	this.iterator = csvStrategy.iterator();
    	this.csvWriter = csvFileState.openCSVWriter();
    	this.csvFileState = csvFileState;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if(csvWriter == null){
            throw new IOException("Writer has been closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        try {
            if(!appending){
                if(iterator.hasNext()){
                    this.currentFeature = iterator.next();
                    return this.currentFeature;
                }
                else {
                    this.appending = true;
                }
            }
            String fid = featureType.getTypeName()+"."+nextRow;
            Object values[] = DataUtilities.defaultValues( featureType );
            
            this.currentFeature = SimpleFeatureBuilder.build( featureType, values, fid );
            return this.currentFeature;
        }
        catch (IllegalArgumentException invalid ){
            throw new IOException("Unable to create feature:"+invalid.getMessage(),invalid);
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        if(this.csvWriter == null){
            throw new IOException("Writer has been closed");
        }
        if (this.appending) {
            return false; // reader has no more contents
        }
        return this.iterator.hasNext();
    }

	@Override
    /**
     * Mark our {@link #currentFeature} feature as null, it will be skipped when written effectively removing it.
     */
	public void remove() throws IOException {
		this.currentFeature = null; // just mark it done which means it will not get written out.
	}
	
    @Override
    public void close() throws IOException {
        if( csvWriter == null ){
            throw new IOException("Writer alread closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        // Step 1: Write out remaining contents (if applicable)
        while (hasNext()) {
            next();
            write();
        }
        csvWriter.close();
        csvWriter = null;
        if( this.iterator != null ){
            this.iterator.close();
            this.iterator = null;
        }
        // Step 2: Replace file contents
        File file = this.csvFileState.getFile();
        
        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING );   
    }

	@Override
	public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }
        for (Property property : currentFeature.getProperties()) {
            Object value = property.getValue();
            if (value == null) {
                this.csvWriter.write("");
            } else if (value instanceof Point) {
                Point point = (Point) value;
                this.csvWriter.write(Double.toString(point.getX()));
                this.csvWriter.write(Double.toString(point.getY()));
            } else {
                String txt = value.toString();
                this.csvWriter.write(txt);
            }
        }
        this.csvWriter.endRecord();
        this.nextRow++;
        this.currentFeature = null; // indicate that it has been written
	}
}
