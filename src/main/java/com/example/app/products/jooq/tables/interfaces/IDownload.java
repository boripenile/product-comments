/*
 * This file is generated by jOOQ.
*/
package com.example.app.products.jooq.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;

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
public interface IDownload extends VertxPojo, Serializable {

    /**
     * Setter for <code>products_download.download.id</code>.
     */
    public IDownload setId(UInteger value);

    /**
     * Getter for <code>products_download.download.id</code>.
     */
    public UInteger getId();

    /**
     * Setter for <code>products_download.download.product_name</code>.
     */
    public IDownload setProductName(String value);

    /**
     * Getter for <code>products_download.download.product_name</code>.
     */
    public String getProductName();

    /**
     * Setter for <code>products_download.download.download_date</code>.
     */
    public IDownload setDownloadDate(Timestamp value);

    /**
     * Getter for <code>products_download.download.download_date</code>.
     */
    public Timestamp getDownloadDate();

    /**
     * Setter for <code>products_download.download.comment_link</code>.
     */
    public IDownload setCommentLink(String value);

    /**
     * Getter for <code>products_download.download.comment_link</code>.
     */
    public String getCommentLink();

    /**
     * Setter for <code>products_download.download.downloaded_by</code>.
     */
    public IDownload setDownloadedBy(String value);

    /**
     * Getter for <code>products_download.download.downloaded_by</code>.
     */
    public String getDownloadedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common interface IDownload
     */
    public void from(com.example.app.products.jooq.tables.interfaces.IDownload from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface IDownload
     */
    public <E extends com.example.app.products.jooq.tables.interfaces.IDownload> E into(E into);

    @Override
    public default IDownload fromJson(io.vertx.core.json.JsonObject json) {
        // Omitting unrecognized type org.jooq.types.UInteger for column id!
        setProductName(json.getString("product_name"));
        // Omitting unrecognized type java.sql.Timestamp for column download_date!
        setCommentLink(json.getString("comment_link"));
        setDownloadedBy(json.getString("downloaded_by"));
        return this;
    }


    @Override
    public default io.vertx.core.json.JsonObject toJson() {
        io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
        // Omitting unrecognized type org.jooq.types.UInteger for column id!
        json.put("product_name",getProductName());
        // Omitting unrecognized type java.sql.Timestamp for column download_date!
        json.put("comment_link",getCommentLink());
        json.put("downloaded_by",getDownloadedBy());
        return json;
    }

}
