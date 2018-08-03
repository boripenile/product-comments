/*
 * This file is generated by jOOQ.
*/
package com.example.app.products.jooq.tables;


import com.example.app.products.jooq.Indexes;
import com.example.app.products.jooq.Keys;
import com.example.app.products.jooq.ProductsDownload;
import com.example.app.products.jooq.tables.records.DownloadRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Download extends TableImpl<DownloadRecord> {

    private static final long serialVersionUID = -2105038308;

    /**
     * The reference instance of <code>products_download.download</code>
     */
    public static final Download DOWNLOAD = new Download();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DownloadRecord> getRecordType() {
        return DownloadRecord.class;
    }

    /**
     * The column <code>products_download.download.id</code>.
     */
    public final TableField<DownloadRecord, UInteger> ID = createField("id", org.jooq.impl.SQLDataType.INTEGERUNSIGNED.nullable(false).identity(true), this, "");

    /**
     * The column <code>products_download.download.product_name</code>.
     */
    public final TableField<DownloadRecord, String> PRODUCT_NAME = createField("product_name", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>products_download.download.download_date</code>.
     */
    public final TableField<DownloadRecord, Timestamp> DOWNLOAD_DATE = createField("download_date", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>products_download.download.comment_link</code>.
     */
    public final TableField<DownloadRecord, String> COMMENT_LINK = createField("comment_link", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>products_download.download.downloaded_by</code>.
     */
    public final TableField<DownloadRecord, String> DOWNLOADED_BY = createField("downloaded_by", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * Create a <code>products_download.download</code> table reference
     */
    public Download() {
        this(DSL.name("download"), null);
    }

    /**
     * Create an aliased <code>products_download.download</code> table reference
     */
    public Download(String alias) {
        this(DSL.name(alias), DOWNLOAD);
    }

    /**
     * Create an aliased <code>products_download.download</code> table reference
     */
    public Download(Name alias) {
        this(alias, DOWNLOAD);
    }

    private Download(Name alias, Table<DownloadRecord> aliased) {
        this(alias, aliased, null);
    }

    private Download(Name alias, Table<DownloadRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return ProductsDownload.PRODUCTS_DOWNLOAD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.DOWNLOAD_COMMENTS_ACCOUNT_FK, Indexes.DOWNLOAD_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<DownloadRecord, UInteger> getIdentity() {
        return Keys.IDENTITY_DOWNLOAD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DownloadRecord> getPrimaryKey() {
        return Keys.KEY_DOWNLOAD_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DownloadRecord>> getKeys() {
        return Arrays.<UniqueKey<DownloadRecord>>asList(Keys.KEY_DOWNLOAD_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<DownloadRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<DownloadRecord, ?>>asList(Keys.COMMENTS_ACCOUNT_FK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Download as(String alias) {
        return new Download(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Download as(Name alias) {
        return new Download(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Download rename(String name) {
        return new Download(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Download rename(Name name) {
        return new Download(name, null);
    }
}