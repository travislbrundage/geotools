:Author: Travis Brundage
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Strategy Patterns
-----------------

Since this tutorial has been written the result has been broken out into a distinct **gt-csv** module. This work has also been forked into service as part of the GeoServer importer module. In GeoServer's fork, many interesting updates were made to add new abilities. The implementation is strictly concerned with reading content (as part of an import process) and has added a nifty CSVStrategy (with implementations for CSVAttributesOnly, CSVLatLonStrategy, CSVSpecifiedLatLngStrategy and SpecifiedWKTStrategy). In this section we'll discuss the new implementation with strategy objects.

CSVDataStore
^^^^^^^^^^^^

CSVDataStore now uses a CSVStrategy and CSVFileState. The CSVFileState holds information about the file we are reading from. CSVStrategy is a generic interface for the strategy objects CSVDataStore can use.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: public class CSVDataStore extends ContentDataStore implements FileDataStore {
      :end-before: public Name getTypeName() {



CSVDataStoreFactory
^^^^^^^^^^^^^^^^^^^

CSVFeatureReader
^^^^^^^^^^^^^^^^

CSVFeatureSource
^^^^^^^^^^^^^^^^

CSVFileState
^^^^^^^^^^^^

AbstractCSVStrategy
^^^^^^^^^^^^^^^^^^^

CSVAttributeOnlyStrategy
^^^^^^^^^^^^^^^^^^^^^^^^

CSVIterator
^^^^^^^^^^^

CSVLatLonStrategy
^^^^^^^^^^^^^^^^^

CSVSpecifiedLatLngStrategy
^^^^^^^^^^^^^^^^^^^^^^^^^^

CSVSpecifiedWKTStrategy
^^^^^^^^^^^^^^^^^^^^^^^

CSVStrategy
^^^^^^^^^^^

CSVStrategySupport
^^^^^^^^^^^^^^^^^^