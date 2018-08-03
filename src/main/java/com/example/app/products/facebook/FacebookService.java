package com.example.app.products.facebook;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.app.products.dto.CommentDTO;
import com.example.app.products.dto.DownloadDTO;
import com.example.app.products.dto.FacebookCommentDTO;
import com.example.app.products.dto.Item;
import com.example.app.products.dto.SnippetDetail;
import com.example.app.products.queries.DownloadQueries;
import com.example.app.products.util.CSVWriter;
import com.example.app.products.util.JSONFlattener;
import com.example.app.products.util.SendEmailService;
import com.example.app.products.util.SimpleMailMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class FacebookService {
	private final Logger LOGGER = LoggerFactory.getLogger(FacebookService.class);

	@Autowired
	private RestTemplate restTemplate;

	private File facebookDir = null;

	@Async
	public void getFacebookComments(String user, String videoUrl) {
		try {
			String videoId = "";
			String video = videoUrl.trim();
			if (video.indexOf("?") == -1) {
				//return CompletableFuture.completedFuture(0);
				return;
			}		
			int position = video.indexOf("?");
			String parts = video.substring(position+1, video.length());

			String nextPageToken = "";
			String url = "";
			List<FacebookCommentDTO> comments = new ArrayList<FacebookCommentDTO>();
			if (parts == null) {
				//return CompletableFuture.completedFuture(0);
				return;
			}
				
			String[] queryParam = parts.split("=");

			LOGGER.info("videoId: " + queryParam[1]);
			
			videoId = queryParam[1];
			
			String key = "AIzaSyArYNr9j6Brl612FndSxrxbaVwx6DkRmpM";
			
			url = setUrl(videoId, nextPageToken, url, key);
			FacebookCommentDTO comment = restTemplate.getForObject(url, FacebookCommentDTO.class);

			int total = 0;
			int grandTotal = 180000;
			
			while (comment.getItems().size() > 0) {
				if (comment.getNextPageToken() != null && !comment.getNextPageToken().isEmpty()) {
					nextPageToken = comment.getNextPageToken();
					total += comment.getItems().size();
					comments.add(comment);	
					if (total == 30000) {
						saveAndSendToEmail(user, comments, videoId);
						comments = new ArrayList<FacebookCommentDTO>();
						total = 0;
					}
				} else {
					nextPageToken = "";
					comments.add(comment);
					System.out.println("Total final: " + total);
					if (total > 0) {
						saveAndSendToEmail(user, comments, videoId);
						comments = new ArrayList<FacebookCommentDTO>();
					}
					break;
				}
				url = setUrl(videoId, nextPageToken, url, key);	
				try {
					comment = restTemplate.getForObject(url, FacebookCommentDTO.class);
				} catch (Exception e) {
					//Comments download completed successful and throw an error
					break;
				}
			}
			
			//return CompletableFuture.completedFuture(0);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return CompletableFuture.completedFuture(0);
		//send a mail here...
		return;
	}

	private void saveAndSendToEmail(String user, List<FacebookCommentDTO> comments, String videoId)
			throws ParseException, AddressException, MessagingException {
		if (comments.size() > 0) {
			if (checkDirectory()) {
				String fileName = "";
				List<CommentDTO> allComments = new ArrayList<CommentDTO>();
				for (FacebookCommentDTO comm : comments) {
					List<CommentDTO> commentsDTO = new ArrayList<CommentDTO>();

					fileName  = processComments(comm, commentsDTO);
					allComments.addAll(commentsDTO);
				}
				
				if (allComments.size() > 0) {
					String fullPath = convertJsonToCsv(allComments, fileName);
					
					if (fullPath != null && !fullPath.isEmpty()) {					
						// Save the downloadable link to database to download later					
						DownloadDTO download = new DownloadDTO(null, "Facebook-"+videoId, 
								new Timestamp(System.currentTimeMillis()), fullPath, user);
						
						Integer id = DownloadQueries.INSTANCE.saveDownload(download);
						if (id > 0) {
							// Email a downloadable link
							//return CompletableFuture.completedFuture(id);
							String strId = String.format("%07d", id);
							ResourceBundle bundle = ResourceBundle.getBundle("application");
							String downloadLink 
								= bundle.getString("downloadLink").concat("/") + strId;
							SimpleMailMessage mail = new SimpleMailMessage();
							Address from = new InternetAddress("boripe2000@gmail.com");
							mail.setFrom(from);
							mail.setTo(new String[] {user});
							mail.setSubject("Video Comments Download Link");
							mail.setText("Please click the following link "
									+ "to download video comments\n\n"+downloadLink);
							
							boolean sent = 
									SendEmailService.INSTANCE.send(mail);
						}
						
					}
				}					
			}
		}
	}

	private String setUrl(String videoId, String nextPageToken, String url, String key) {
		if (nextPageToken.isEmpty()) {
			url = "https://www.googleapis.com/youtube/v3/commentThreads?textFormat=plainText&part=snippet&videoId=" + videoId
					+ "&key="+key+"&maxResults=100";
		} else {
			if (!nextPageToken.isEmpty()) {
				url = "https://www.googleapis.com/youtube/v3/commentThreads?textFormat=plainText&part=snippet&videoId=" + videoId
						+ "&key="+key+"&maxResults=100"+"&pageToken="+nextPageToken;
			}
		}
		return url;
	}

	private boolean checkDirectory() {
		try {
			String pathName = System.getProperty("user.dir");
			File locationDir = new File(pathName);
			if (locationDir.isDirectory()) {
				facebookDir = new File(locationDir, "facebook");
				if (!facebookDir.exists()) {
					facebookDir.mkdir();
					LOGGER.info("Created: {}", facebookDir.getAbsolutePath());
				}
				LOGGER.info("Exist: {}", facebookDir.getAbsolutePath());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String convertJsonToCsv(List<CommentDTO> comments, String fileName) {
		try {

			//List<CommentDTO> comments = new ArrayList<CommentDTO>();

			Type listType = new TypeToken<List<CommentDTO>>() {}.getType();
			//String fileName = processComments(comment, comments);
			String fullPath = facebookDir.getAbsolutePath() + File.separatorChar + fileName + ".csv";

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
			//CSVWriter.writeToFile(CSVWriter.getCSV(flatJson, ";"), fullPath);
			
			LOGGER.info("Full Path: {}", fullPath);
			return fullPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String processComments(FacebookCommentDTO comment, List<CommentDTO> comments) throws ParseException {
		String fileName = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMMM d, yyyy");

		for (Item item : comment.getItems()) {
			if (item.getSnippet().getTopLevelComment().getSnippet() != null) {
				if (fileName.isEmpty()) {
					fileName = item.getId();
				}
				SnippetDetail detail = item.getSnippet().getTopLevelComment().getSnippet();
				Date publishDate = dateFormat.parse(detail.getPublishedAt());
				String displayDate = displayDateFormat.format(publishDate);

				CommentDTO commentDto = new CommentDTO(detail.getAuthorDisplayName(), 
						displayDate, "", detail.getTextDisplay(), "");
				
				comments.add(commentDto);
			}
		}
		return fileName;
	}

}
