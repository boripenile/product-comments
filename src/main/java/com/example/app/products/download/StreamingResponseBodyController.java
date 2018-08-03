package com.example.app.products.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.app.products.dto.DownloadDTO;
import com.example.app.products.queries.DownloadQueries;

@Controller 
public class StreamingResponseBodyController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(StreamingResponseBodyController.class);
	
    @RequestMapping(value = "/downloadFile/{id}", method = RequestMethod.GET)
    public StreamingResponseBody getSteamingFile(HttpServletResponse response, 
    		@PathVariable("id") String id) throws IOException {
    	LOGGER.info("Id: {}", id);
    	Integer intId = Integer.parseInt(id);
    	UInteger convertedId = UInteger.valueOf(intId);
    			
    	DownloadDTO download = DownloadQueries.INSTANCE.findDownloadById(convertedId);
    	
    	if (download != null) {
    		LOGGER.info(download.toString());
    		
    		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
    		String date = dateFormat.format(download.downloadDate);
    		
    		response.setContentType("text/csv");
    		String fileName = download.productName
    				.concat(download.id.toString()).concat("-")
    				.concat(date).concat(".csv");
    		
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            
            @SuppressWarnings("resource")
			InputStream inputStream = new FileInputStream(new File(download.commentLink));
            return outputStream -> {
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
            };
    	}
        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(Integer.parseInt("0001"));
	}
}