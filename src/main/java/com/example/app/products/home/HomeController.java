package com.example.app.products.home;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.app.products.amazon.AmazonService;
import com.example.app.products.download.DownloadForm;
import com.example.app.products.dto.DownloadJsonRespone;
import com.example.app.products.facebook.FacebookService;

@Controller
class HomeController {

	private static final String HOME_VIEW_NAME = "home/homeSignedIn";

	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private AmazonService amazonService;

	@ModelAttribute("module")
	String module() {
		return "home";
	}

	@GetMapping("/")
	String index(Principal principal, Model model) {
		model.addAttribute(new DownloadForm());
		return principal != null ? HOME_VIEW_NAME : "home/homeNotSignedIn";
	}

	@PostMapping(value = "/download", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public DownloadJsonRespone index(@Valid @RequestBody DownloadForm downloadForm, Errors errors,
			Principal principal, HttpServletResponse response) {
		DownloadJsonRespone respone = new DownloadJsonRespone();
		respone.setValidated(false);
		try {
			if (errors.hasErrors()) {
				//Get error message
		         Map<String, String> error = errors.getFieldErrors().stream()
		               .collect(
		                     Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
		                 );		         
		         respone.setValidated(false);
		         respone.setErrorMessages(error);
			} else {
				if (downloadForm.getDownloadUrl().contains("youtube.com")) {
					facebookService.getFacebookComments(principal.getName(),
							downloadForm.getDownloadUrl());
					respone.setValidated(true);
					return respone;
				} else if (downloadForm.getDownloadUrl().contains("amazon.com")) {
					amazonService.getAmazonReviews(principal.getName(),
							downloadForm.getDownloadUrl());
					respone.setValidated(true);
					return respone;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respone;
	}
}
