package com.mediscreen.clientui.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * Error Controller bean that'll replace the default SpringBoot white-label error page.
 * 
 * <p>
 * Baeldung: springboot <a href="https://www.baeldung.com/spring-boot-custom-error-page"> custom error-page</a>
 * </p>
 * 
 * @author jerome
 *
 */

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

	
	@RequestMapping("/error") //note: if we use @GetMapping, errors that occured on POST do not display because "Request method 'POST' not supported" ad we get a blank page...
	public String handleError(HttpServletRequest request, Model model) {
		
		log.error("CustomErrorController: @RequestMapping(\"/error\")");
		
		Integer status = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		log.error("Error with status code {} happened, ERROR_MESSAGE={}", status, request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
		
		
		String errorMsg;
		switch(status){
	       case 404:
	    	   errorMsg = "Error 404, Page not found!";
	           break;
	       case 403:
	    	   errorMsg = "Error 403, Access denied!";
	           break;
	       case 500:
	    	   errorMsg = "Sorry, error 500 happened. Please contact our support!";
	           break;
	       default:
	    	   errorMsg = "Sorry an error has happened.";
	           break;
		}
				
    	model.addAttribute("errorMsg", errorMsg);
	    return "error";
	}
}
