package com.mediscreen.mdiabeteassess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.mdiabeteassess.service.DiabeteAssessService;

import io.swagger.annotations.ApiOperation;

@RestController
public class DiabeteAssessController {
	
	@Autowired
	private DiabeteAssessService diabeteAssessService;
	
	/**
	 * Assess diabete for a patient by id
	 * @param patId the patient id
	 * @return the assessment for diabete
	 */
	@ApiOperation(value = "This endpoint returns the diabete assessment for a patient id.")
	@PostMapping("/assess/id")
	public String postAssessById(@RequestParam Integer patId){
		
		return diabeteAssessService.diabeteAssessCalculate(patId);
		
	}
	
	/**
	 * Assess diabete for a patient by family name.
	 * @param familyname the patient family name
	 * @return the assessment for diabete
	 */
	@PostMapping("/assess/familyName")
	public String postPatientForm(@RequestParam String familyname){

		return diabeteAssessService.diabeteAssessCalculateByFamilyName(familyname);
		
	}

}
