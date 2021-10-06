package com.mediscreen.clientui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mediscreen.clientui.proxy.DiabeteAssessProxyFeign;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DiabeteAssessController {
	
	@Autowired
	private DiabeteAssessProxyFeign diabeteAssessProxy;
	
	@Autowired
	ObjectMapper mapper;
	
	@GetMapping(value="/patients/{id}/diabeteAssess",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectNode patientdiabeteAssess(@PathVariable Integer id){

		String assess = diabeteAssessProxy.postAssessById(id);
		log.info("DiabeteAssessController : {}",assess);
		ObjectNode diabeteAssessNode = mapper.createObjectNode();
		diabeteAssessNode.put("message",assess);

		return diabeteAssessNode;
	}


}
