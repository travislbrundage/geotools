Vertica Plugin
--------------

Supports direct access to an HP Vertica database.

References:

Related

* https://my.vertica.com/docs/8.0.x/HTML/index.htm HP Vertica version 8.0.x documentation which this plugin was developed against.

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-vertica</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

**Basic connection parameters**

+-------------+----------------------------------------------+
| Parameter   | Description                                  |
+=============+==============================================+
| "dbtype"    | Must be the string "vertica"                 |
+-------------+----------------------------------------------+
| "host"      | Machine name or IP address to connect to     |
+-------------+----------------------------------------------+
| "port"      | Port number to connect to, default 5433      |
+-------------+----------------------------------------------+
| "database"  | The database to connect to                   |
+-------------+----------------------------------------------+
| "user"      | User name                                    |
+-------------+----------------------------------------------+
| "passwd"    | Password                                     |
+-------------+----------------------------------------------+

Creating
^^^^^^^^

Here is a quick example:

.. literalinclude:: /../src/main/java/org/geotools/jdbc/JDBCExamples.java
:language: java
       :start-after: // verticaExample start
       :end-before: // verticaExample end

    The above will reference a database file named "database" located in the current working directory.