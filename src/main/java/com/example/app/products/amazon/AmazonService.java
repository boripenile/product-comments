package com.example.app.products.amazon;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.app.products.dto.CommentDTO;
import com.example.app.products.dto.DownloadDTO;
import com.example.app.products.queries.DownloadQueries;
import com.example.app.products.util.CSVWriter;
import com.example.app.products.util.CodeHelper;
import com.example.app.products.util.JSONFlattener;
import com.example.app.products.util.SendEmailService;
import com.example.app.products.util.SimpleMailMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class AmazonService {
	private final Logger LOGGER = LoggerFactory.getLogger(AmazonService.class);

	private File amazonDir = null;

	@Async
	public void getAmazonReviews(String user, String productUrl) {
		try {
			List<CommentDTO> reviews = new ArrayList<>();
			productUrl = productUrl.replace(" ", "");
			fetchProductReviews(productUrl, reviews, user);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	private void saveAndSendToEmail(String user, String productCode, List<CommentDTO> reviews)
			throws AddressException, MessagingException {
		if (reviews.size() > 0) {
			if (checkDirectory()) {
				String fullPath = convertJsonToCsv(reviews);
				
				if (fullPath != null && !fullPath.isEmpty()) {					
					DownloadDTO download = new DownloadDTO(null, "Amazon-"+productCode, 
							new Timestamp(System.currentTimeMillis()), fullPath, user);
					
					Integer id = DownloadQueries.INSTANCE.saveDownload(download);
					if (id > 0) {
						//return CompletableFuture.completedFuture(id);
						String strId = String.format("%07d", id);
						ResourceBundle bundle = ResourceBundle.getBundle("application");
						String downloadLink 
							= bundle.getString("downloadLink").concat("/") + strId;
						SimpleMailMessage mail = new SimpleMailMessage();
						Address from = new InternetAddress("boripe2000@gmail.com");
						mail.setFrom(from);
						mail.setTo(new String[] {user});
						mail.setSubject("Product Reviews Download Link");
						mail.setText("Please click the following link "
								+ "to download product reviews\n\n"+downloadLink);
						
						boolean sent = 
								SendEmailService.INSTANCE.send(mail);
					}
					
				}
			}
		}
	}

	private void fetchProductReviews(String productUrl, List<CommentDTO> reviews, String user) 
			throws IOException, AddressException, MessagingException {
		int pageNumber = 0;
		String urlToParse = "";
		String prefixRef = "ref=cm_cr_arp_d_paging_btm_";
		String prefixQuery = "?ie=UTF8&reviewerType=all_reviews";
		String productPath = "";
		String productCode = "";

		if (productUrl.indexOf("/B") != -1) {
			String processProductUrl = productUrl;
			int preLocation = processProductUrl.indexOf("/B");
			productPath = "https://www.amazon.com/";
			productCode = productUrl.substring(preLocation, preLocation+11).replace("/", "");
			urlToParse = productPath + "product-reviews/" + productCode + "/" + prefixRef;
			System.out.println(productCode);
		} 
		int total = 0;
		while (true) {
			if (urlToParse.isEmpty()) break;
			if (pageNumber == 0) {
				pageNumber += 1;
			}
			
		    String url = urlToParse + pageNumber + prefixQuery + "&pageNumber=" + pageNumber;
		    //LOGGER.info("Crawling URL: {}", url);

		    Document doc = Jsoup.connect(url).timeout(60*1000).get();
		    
		    Elements reviewElements = doc.select(".review");
		    if (reviewElements == null || reviewElements.isEmpty()) {
		    	System.out.println("It is empty");
		    	System.out.println(doc);
		        break;
		    }
		    for (Element reviewElement : reviewElements) {
		    	String rating = "";
		        Element textElement = reviewElement.select(".review-text").first();
		        if (textElement == null) {
		            continue;
		        }
		        String text = textElement.text();
		        Element dateElement = reviewElement.select(".review-date").first();
		        if (dateElement == null) {
		            continue;
		        } 
		        String date = dateElement.text();
		        
		        Element ratingElement = reviewElement.select(".review-rating").first();
		        if (ratingElement == null) {
		            continue;
		        } 
		        if (ratingElement != null) {
		        	rating = ratingElement.text();
		        }
		        Element profileNameElement = reviewElement.select(".author").first();
		        String profileName = "";
		        if (profileNameElement == null) {
		        	profileName = "";
		        } else {
		        	profileName = profileNameElement.text();
		        }
		        
		        reviews.add(new CommentDTO(profileName, date, rating, text, ""));
		    }
		    total += reviews.size();
		    if (total >= 50000) {
		    	System.out.println(total);
		    	saveAndSendToEmail(user, productCode, reviews);
		    	reviews = new ArrayList<>();
		    	total = 0;
		    }
		    pageNumber++;
		}
		if (total > 0) {
			System.out.println(total);
			saveAndSendToEmail(user, productCode, reviews);
		}
	}
	
	private boolean checkDirectory() {
		try {
			String pathName = System.getProperty("user.dir");
			File locationDir = new File(pathName);
			if (locationDir.isDirectory()) {
				amazonDir = new File(locationDir, "amazon");
				if (!amazonDir.exists()) {
					amazonDir.mkdir();
					LOGGER.info("Created: " + amazonDir.getAbsolutePath());
				}
				LOGGER.info("Exist: " + amazonDir.getAbsolutePath());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String convertJsonToCsv(List<CommentDTO> comment) {
		try {

			List<CommentDTO> comments = comment;

			Type listType = new TypeToken<List<CommentDTO>>() {}.getType();
			
			String fileName = processComments(comments);
			
			String fullPath = amazonDir.getAbsolutePath() + File.separatorChar + fileName + ".csv";

			Gson gson = new Gson();
			String jsonString = gson.toJson(comments, listType);
			/*
			 * Parse a JSON String and convert it to CSV
			 */
			List<Map<String, String>> flatJson = JSONFlattener.parseJson(jsonString);
			// Using the default separator ','
			// If you want to use an other separator like ';' or '\t' use
			// CSVWriter.getCSV(flatJSON, separator) method
			List<Map<String, String>> headers = new ArrayList<Map<String, String>>();
			Map<String, String> username = new HashMap<>();
			username.put("userName","UserName");
			headers.add(username);
			Map<String, String> date = new HashMap<>();
			date.put("date","Date");
			headers.add(date);
			Map<String, String> rating = new HashMap<>();
			rating.put("starRating","Star rating");
			headers.add(rating);
			Map<String, String> commen = new HashMap<>();
			commen.put("comment","Review or Comment");
			headers.add(commen);
			Map<String, String> link = new HashMap<>();
			link.put("link","Link");
			headers.add(link);
			CSVWriter.writeLargeFile(flatJson, ";", fullPath, CSVWriter.collectOrderedHeaders(headers));
			
			//CSVWriter.writeToFile(CSVWriter.getCSV(flatJson, "\t"), fullPath);
			LOGGER.info("Full Path: " + fullPath);
			return fullPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String processComments(List<CommentDTO> comments) throws ParseException {
		return CodeHelper.genCode(15);
	}

}
