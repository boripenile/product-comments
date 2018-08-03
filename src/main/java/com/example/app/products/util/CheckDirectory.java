package com.example.app.products.util;

import java.io.File;

public class CheckDirectory {
	
	File productDownloadDirectory = null;
	
	public CheckDirectory(String productName) {
		String basePath = Utility.getBasePath();		
		File homeDir = new File(basePath);
		
		if (!homeDir.exists()) {
			homeDir.mkdirs();
		}		
		if (homeDir.exists() && homeDir.isDirectory()) {
			System.out.println(homeDir.getAbsolutePath());
			productDownloadDirectory = new File(homeDir, productName);
			if (!productDownloadDirectory.exists()) {
				productDownloadDirectory.mkdir();
				System.out.println(productDownloadDirectory.getAbsolutePath());
			}
		}
	}
	
	public File getProductDownloadDirectory() {
		return this.productDownloadDirectory;
	}
	
}
