/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.sql.databasemetadata;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableName", "tableType", "remarks", "typeCat", "typeSchem", "typeName",
        "selfReferencingColName", "refGeneration",
        "bestRowIdentifiers", "columns", "columnPrivileges", "exportedKeys",
        "importedKeys", "indexInfo", "primaryKeys", "tablePrivileges",
        "versionColumns"
    }
)
public class Table implements Comparable<Table> {


    /**
     * Suppression path value for {@link #bestRowIdentifiers}.
     */
    public static final String SUPPRESSION_PATH_BEST_ROW_IDENTIFIERS
        = "table/bestRowIdentifiers";


    /**
     * Suppression path value for {@link #columns}.
     */
    public static final String SUPPRESSION_PATH_COLUMNS
        = "table/columns";


    /**
     * Suppression path value for {@link #columnPrivileges}.
     */
    public static final String SUPPRESSION_PATH_COLUMN_PRIVILEGES
        = "table/columnPrivileges";


    /**
     * Suppression path value for {@link #exportedKeys}.
     */
    public static final String SUPPRESSION_PATH_EXPORTED_KEYS
        = "table/exportedKeys";


    /**
     * Suppression path value for {@link #exportedKeys}.
     */
    public static final String SUPPRESSION_PATH_IMPORTED_KEYS
        = "table/importedKeys";


    /**
     * Suppression path value for {@link #indexInfo}.
     */
    public static final String SUPPRESSION_PATH_INDEX_INFO
        = "table/indexInfo";


    /**
     * Suppression path value for {@link #primaryKeys}.
     */
    public static final String SUPPRESSION_PATH_PRIMARY_KEYS
        = "table/primaryKeys";


    /**
     * Suppression path value for {@link #tablePrivileges}.
     */
    public static final String SUPPRESSION_PATH_TABLE_PRIVILEGES
        = "table/tablePrivileges";


    /**
     * Suppression path value for {@link #versionColumns}.
     */
    public static final String SUPPRESSION_PATH_VERSION_COLUMNS
        = "table/versionColumns";


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @param tables
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog,
                                final String schemaPattern,
                                final String tableNamePattern,
                                final String[] types,
                                final Collection<? super Table> tables)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (tables == null) {
            throw new NullPointerException("null tables");
        }

        if (suppression.isSuppressed(Schema.SUPPRESSION_PATH_TABLES)) {
            return;
        }

        final ResultSet resultSet = database.getTables(
            catalog, schemaPattern, tableNamePattern, types);
        try {
            while (resultSet.next()) {
                tables.add(ColumnRetriever.retrieve(
                    Table.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Schema schema)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (schema == null) {
            throw new NullPointerException("null schema");
        }

        retrieve(database, suppression,
                 schema.getCatalog().getTableCat(), schema.getTableSchem(),
                 null, null,
                 schema.getTables());

        for (final Table table : schema.getTables()) {
            table.setSchema(schema);
        }

        for (final Table table : schema.getTables()) {
            BestRowIdentifier.retrieve(database, suppression, table);
            Column.retrieve(database, suppression, table);
            ColumnPrivilege.retrieve(database, suppression, table);
            ExportedKey.retrieve(database, suppression, table);
            ImportedKey.retrieve(database, suppression, table);
            IndexInfo.retrieve(database, suppression, table);
            PrimaryKey.retrieve(database, suppression, table);
            TablePrivilege.retrieve(database, suppression, table);
            VersionColumn.retrieve(database, suppression, table);
        }
    }


    /**
     * Creates a new instance.
     */
    public Table() {

        super();
    }


    @Override
    public int compareTo(final Table o) {

        if (o == null) {
            throw new NullPointerException("object");
        }

        final int tableTypeCompared = tableType.compareTo(o.tableType);
        if (tableTypeCompared != 0) {
            return tableTypeCompared;
        }

        final int schemaCompared = schema.compareTo(o.schema);
        if (schemaCompared != 0) {
            return schemaCompared;
        }

        return tableName.compareTo(o.tableName);
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // --------------------------------------------------------------- tableName
    public String getTableName() {

        return tableName;
    }


    public void setTableName(final String tableName) {

        this.tableName = tableName;
    }


    // --------------------------------------------------------------- tableType
    public String getTableType() {

        return tableType;
    }


    public void setTableType(final String tableType) {

        this.tableType = tableType;
    }


    // ----------------------------------------------------------------- REMARKS
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // -------------------------------------------------- selfReferencingColName
    public String getSelfReferencingColName() {

        return selfReferencingColName;
    }


    public void setSelfReferencingColName(final String selfReferencingColName) {

        this.selfReferencingColName = selfReferencingColName;
    }


    // ----------------------------------------------------------- refGeneration
    public String getRefGeneration() {

        return refGeneration;
    }


    public void setRefGeneration(final String refGeneration) {

        this.refGeneration = refGeneration;
    }


    // ------------------------------------------------------ bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {

        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<BestRowIdentifier>();
        }

        return bestRowIdentifiers;
    }


    // ----------------------------------------------------------------- columns
    public List<Column> getColumns() {

        if (columns == null) {
            columns = new ArrayList<Column>();
        }

        return columns;
    }


    /**
     * Returns a list of column names ordered by {@code ordinalPosition}.
     *
     * @return a list of column names.
     */
    public List<String> getColumnNames() {

        final List<String> columnNames
            = new ArrayList<String>(getColumns().size());

        for (final Column column : getColumns()) {
            columnNames.add(column.getColumnName());
        }

        return columnNames;
    }


    public Column getColumnByName(final String columnName) {

        if (columnName == null) {
            throw new NullPointerException("columnName");
        }

        for (final Column column : getColumns()) {
            if (columnName.equals(column.getColumnName())) {
                return column;
            }
        }

        return null;
    }


    // -------------------------------------------------------- columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {

        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<ColumnPrivilege>();
        }

        return columnPrivileges;
    }


    // ------------------------------------------------------------ exportedKeys
    public List<ExportedKey> getExportedKeys() {

        if (exportedKeys == null) {
            exportedKeys = new ArrayList<ExportedKey>();
        }

        return exportedKeys;
    }


    // ------------------------------------------------------------ importedKeys
    public List<ImportedKey> getImportedKeys() {

        if (importedKeys == null) {
            importedKeys = new ArrayList<ImportedKey>();
        }

        return importedKeys;
    }


    // ----------------------------------------------------------------- indices
    public List<IndexInfo> getIndexInfo() {

        if (indexInfo == null) {
            indexInfo = new ArrayList<IndexInfo>();
        }

        return indexInfo;
    }


    public List<String> getIndexColumnNames(final String indexName) {

        if (indexName == null) {
            throw new NullPointerException("null indexName");
        }

        final List<String> indexColumnNames = new ArrayList<String>();

        for (final IndexInfo indexInfo_ : getIndexInfo()) {
            if (!indexName.equals(indexInfo_.getIndexName())) {
                continue;
            }
            indexColumnNames.add(indexInfo_.getColumnName());
        }

        return indexColumnNames;
    }


    public List<String> getIndexNames(final Boolean nonUnique,
                                      final Short... types) {

        final List<String> indexNames = new ArrayList<String>();

        for (final IndexInfo indexInfo_ : getIndexInfo()) {
            if (nonUnique != null
                && indexInfo_.getNonUnique() != nonUnique.booleanValue()) {
                continue;
            }
            if (types != null) {
                boolean typeMatches = false;
                for (final Short type : types) {
                    if (type == null) {
                        throw new NullPointerException("null type");
                    }
                    if (type.shortValue() == indexInfo_.getType()) {
                        typeMatches = true;
                        break;
                    }
                }
                if (!typeMatches) {
                    continue;
                }
            }
            final String indexName = indexInfo_.getIndexName();
            if (indexNames.contains(indexName)) {
                continue;
            }
            indexNames.add(indexName);
        }

        return indexNames;
    }


    // ------------------------------------------------------------- primaryKey
    public List<PrimaryKey> getPrimaryKeys() {

        if (primaryKeys == null) {
            primaryKeys = new ArrayList<PrimaryKey>();
        }

        return primaryKeys;
    }


    // --------------------------------------------------------- tablePrivileges
    public List<TablePrivilege> getTablePrivileges() {

        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<TablePrivilege>();
        }

        return tablePrivileges;
    }


    // ---------------------------------------------------------- versionColumns
    public List<VersionColumn> getVersionColumns() {

        if (versionColumns == null) {
            versionColumns = new ArrayList<VersionColumn>();
        }

        return versionColumns;
    }


    /**
     * table catalog (may be {@code null}).
     */
    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("table/tableCat")
    @XmlAttribute
    private String tableCat;


    /**
     * table schema (may be {@code null}).
     */
    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("table/tableSchem")
    @XmlAttribute
    private String tableSchem;


    /**
     * parent schema.
     */
    @XmlTransient
    private Schema schema;


    /**
     * table name.
     */
    @ColumnLabel("TABLE_NAME")
    @XmlElement(required = true)
    String tableName;


    /**
     * table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL
     * TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    @ColumnLabel("TABLE_TYPE")
    @XmlElement(required = true)
    String tableType;


    /**
     * explanatory comment on the table.
     */
    @ColumnLabel("REMARKS")
    @XmlElement(nillable = true, required = true)
    String remarks;


    /**
     * the types catalog (may be {@code null}).
     */
    @ColumnLabel("TYPE_CAT")
    @SuppressionPath("table/typeCat")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String typeCat;


    /**
     * the types schema (may be {@code null}).
     */
    @ColumnLabel("TYPE_SCHEM")
    @SuppressionPath("table/typeSchem")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String typeSchem;


    /**
     * type name (may be {@code null}).
     */
    @ColumnLabel("TYPE_NAME")
    @SuppressionPath("table/typeName")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String typeName;


    /**
     * name of the designated "identifier" column of a typed table (may be
     * {@code null}).
     */
    @ColumnLabel("SELF_REFERENCING_COL_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String selfReferencingColName;


    /**
     * specifies how values in
     * {@link #selfReferencingColName SELF_REFERENCING_COL_NAME} are created.
     * Values are "SYSTEM", "USER", "DERIVED". (may be {@code null}).
     */
    @ColumnLabel("REF_GENERATION")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String refGeneration;


    /**
     * bestRowIdentifiers.
     */
    @SuppressWarnings(SUPPRESSION_PATH_BEST_ROW_IDENTIFIERS)
    @XmlElement(name = "bestRowIdentifier")
    List<BestRowIdentifier> bestRowIdentifiers;


    /**
     * columns.
     */
    @SuppressWarnings(SUPPRESSION_PATH_COLUMNS)
    @XmlElement(name = "column")
    List<Column> columns;


    /**
     * columnPrivileges.
     */
    @SuppressionPath(SUPPRESSION_PATH_COLUMN_PRIVILEGES)
    @XmlElement(name = "columnPrivilege")
    List<ColumnPrivilege> columnPrivileges;


    /**
     * exportedKeys.
     */
    @SuppressionPath(SUPPRESSION_PATH_EXPORTED_KEYS)
    @XmlElement(name = "exportedKey")
    List<ExportedKey> exportedKeys;


    /**
     * importedKeys.
     */
    @SuppressionPath(SUPPRESSION_PATH_IMPORTED_KEYS)
    @XmlElement(name = "importedKey")
    List<ImportedKey> importedKeys;


    /**
     * indexInfo.
     */
    @SuppressionPath(SUPPRESSION_PATH_INDEX_INFO)
    @XmlElement(name = "indexInfo")
    List<IndexInfo> indexInfo;


    /**
     * primaryKeys.
     */
    @SuppressWarnings(SUPPRESSION_PATH_PRIMARY_KEYS)
    @XmlElement(name = "primaryKey")
    List<PrimaryKey> primaryKeys;


    /**
     * tablePrivileges.
     */
    @SuppressionPath(SUPPRESSION_PATH_TABLE_PRIVILEGES)
    @XmlElement(name = "tablePrivilege")
    List<TablePrivilege> tablePrivileges;


    /**
     * versionColumns.
     */
    @SuppressionPath(SUPPRESSION_PATH_VERSION_COLUMNS)
    @XmlElement(name = "versionColumn")
    List<VersionColumn> versionColumns;


}

