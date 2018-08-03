package com.example.app.products.util;

public class Utility {
	
	public static String getBasePath(){
		String basePath = null;
		
		switch (OSUtil.getOS()) {
		case WINDOWS:
			basePath = "C:\\temp";
                        break;
		case MAC:
			basePath = "/usr/temp";
                        break;
		case LINUX:
			basePath = "/usr/temp";
                        break;
		case SOLARIS:
			basePath = "/usr/temp";
                        break;
		default:
			break;
		}	
		return basePath;
        }
	
	public static void main(String[] args) {

	}
	
}
