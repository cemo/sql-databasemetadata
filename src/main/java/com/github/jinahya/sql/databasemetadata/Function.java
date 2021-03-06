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
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "functionName", "remarks", "functionType", "specificName"
    }
)
public class Function {


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param functionNamePattern
     * @param functions
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getFunctions(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog,
                                final String schemaPattern,
                                final String functionNamePattern,
                                final Collection<? super Function> functions)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (functions == null) {
            throw new NullPointerException("null functions");
        }

        if (suppression.isSuppressed(Schema.SUPPRESSION_PATH_FUNCTIONS)) {
            return;
        }

        final ResultSet resultSet = database.getFunctions(
            catalog, schemaPattern, functionNamePattern);
        try {
            while (resultSet.next()) {
                functions.add(ColumnRetriever.retrieve(
                    Function.class, suppression, resultSet));
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
                 null,
                 schema.getFunctions());

        for (final Function function : schema.getFunctions()) {
            function.setSchema(schema);
        }
    }


    /**
     * Creates a new instance.
     */
    public Function() {

        super();
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // ------------------------------------------------------------ functionName
    public String getFunctionName() {

        return functionName;
    }


    public void setFuntionName(final String functionName) {

        this.functionName = functionName;
    }


    // ----------------------------------------------------------------- remarks
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    public short getFunctionType() {

        return functionType;
    }


    public void setFunctionType(short functionType) {

        this.functionType = functionType;
    }


    public String getSpecificName() {

        return specificName;
    }


    public void setSpecificName(final String specificName) {

        this.specificName = specificName;
    }


    @ColumnLabel("FUNCTION_NAME")
    @SuppressionPath("function/functionCat")
    @XmlAttribute
    private String functionCat;


    @ColumnLabel("FUNCTION_NAME")
    @SuppressionPath("function/functionSchem")
    @XmlAttribute
    private String functionSchem;


    @XmlTransient
    private Schema schema;


    @ColumnLabel("FUNCTION_NAME")
    @XmlElement(required = true)
    String functionName;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    String remarks;


    @ColumnLabel("FUNCTION_TYPE")
    @XmlElement(required = true)
    short functionType;


    @ColumnLabel("SPECIFIC_NAME")
    @XmlElement(required = true)
    String specificName;


}

