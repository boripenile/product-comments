package com.example.app.products.dto;

import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import java.util.Date;

import org.jooq.types.UInteger;

public class DownloadDTO {

	public final UInteger   id;
	public final String    productName;
	public final Date 	   downloadDate;
	public final String    commentLink;
	public final String    downloadedBy;
    
	@ConstructorProperties({"id", "productName", "downloadDate", "commentLink", "downloadedBy"})
    public DownloadDTO(UInteger id, String productName, Timestamp downloadDate, 
    		String commentLink, String downloadedBy) {
		// TODO Auto-generated constructor stub
		this.id = id;
    	this.productName = productName;
    	this.downloadDate = new Date(downloadDate.getTime());
    	this.commentLink = commentLink;
    	this.downloadedBy = downloadedBy;
	}

	@Override
	public String toString() {
		return "DownloadDTO [id=" + id + ", productName=" + productName + ", downloadDate=" + downloadDate
				+ ", commentLink=" + commentLink + ", downloadedBy=" + downloadedBy + "]";
	}
	
}
