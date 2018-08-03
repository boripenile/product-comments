package com.example.app.products.queries;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import com.example.app.products.dto.DownloadDTO;
import com.example.app.products.jooq.ProductsDownload;
import com.example.app.products.util.ConnectionUtil;

public enum DownloadQueries {

	INSTANCE;
	
	public Integer saveDownload(DownloadDTO download) {
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {			
			UInteger id = context
					.insertInto(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD)
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.COMMENT_LINK, 
							download.commentLink)
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.DOWNLOAD_DATE, new Timestamp(System.currentTimeMillis()))
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.DOWNLOADED_BY, download.downloadedBy)
					.set(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.PRODUCT_NAME, download.productName)
					.returning().fetchOne().getId();
			return id.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public DownloadDTO findDownloadById(UInteger id) {
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {			
			DownloadDTO download = context.select()
					.from(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.ID.eq(id))
					.fetchOne().into(DownloadDTO.class);
			return download;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DownloadDTO> findDownloads() {
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {			
			List<DownloadDTO> downloads = context.select()
					.from(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD)
					.fetch().into(DownloadDTO.class);
			return downloads;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public List<DownloadDTO> findDownloadsByProductName(String productName) {
		try (DSLContext context = DSL.using(ConnectionUtil.INSTANCE.getConfiguration())) {			
			List<DownloadDTO> downloads = context.select()
					.from(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD)
					.where(ProductsDownload.PRODUCTS_DOWNLOAD.DOWNLOAD.PRODUCT_NAME.eq(productName))
					.fetch().into(DownloadDTO.class);
			return downloads;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
