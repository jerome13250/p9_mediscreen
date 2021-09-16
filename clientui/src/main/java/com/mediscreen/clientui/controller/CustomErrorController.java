package com.mediscreen.clientui.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error") //note: if we use @GetMapping, errors that occured on POST do not display because "Request method 'POST' not supported" ad we get a blank page...
	public String handleError(HttpServletRequest request, Model model) {
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		model.addAttribute("status", status);
		log.error("Error with status code {} happened, ERROR_MESSAGE={}", status, request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
	    return "error";
	}
}