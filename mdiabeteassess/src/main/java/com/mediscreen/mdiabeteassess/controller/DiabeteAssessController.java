package com.mediscreen.mdiabeteassess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.mdiabeteassess.service.DiabeteAssessService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class DiabeteAssessController {
	
	@Autowired
	private DiabeteAssessService diabeteAssessService;
	
	/**
	 * Assess diabete for a patient by id
	 * @param patientIdBean the bean that contains patId the patient id
	 * @return the assessment for diabete
	 */
	@ApiOperation(value = "This endpoint returns the diabete assessment for a patient id.")
	@PostMapping("/assess/id")
	public String postAssessById(
			@ApiParam(
					value = "Patient id",
					example = "1")
			@RequestBody Integer patId){
		
		return diabeteAssessService.diabeteAssessCalculate(patId);
		
	}
	
	/**
	 * Assess diabete for a patient by family name.
	 * @param familyNameBean the bean that contains patient family name
	 * @return the assessment for diabete
	 */
	@ApiOperation(value = "This endpoint returns the diabete assessment for a patient family name.")
	@PostMapping("/assess/familyName")
	public String postPatientForm(
			@ApiParam(
					value = "Patient family name",
					example = "TestNone")
			@RequestBody String familyName){

		return diabeteAssessService.diabeteAssessCalculateByFamilyName(familyName);
		
	}

}
