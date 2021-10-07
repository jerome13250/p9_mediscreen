package com.mediscreen.clientui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.clientui.proxy.DiabeteAssessProxyFeign;

@RestController
public class DiabeteAssessController {
	
	@Autowired
	private DiabeteAssessProxyFeign diabeteAssessProxy;
	
	@GetMapping(value="/patients/{id}/diabeteAssess",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public String patientdiabeteAssess(@PathVariable Integer id){

		String assess = diabeteAssessProxy.postAssessById(id);
		
		return assess;
	}


}
