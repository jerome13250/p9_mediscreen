package com.mediscreen.mdiabeteassess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.common.dto.PatientBean;
import com.mediscreen.mdiabeteassess.proxy.MicroservicePatientsProxyFeign;
import com.mediscreen.mdiabeteassess.service.DiabeteAssessService;

@RestController
public class DiabeteAssessController {
	
	@Autowired
	private MicroservicePatientsProxyFeign patientProxy;

	@Autowired
	private DiabeteAssessService diabeteAssessService;
	
	@PostMapping("/assess/id")
	public String postAssessById(@RequestParam Integer patId){
		
		return diabeteAssessService.diabeteAssessCalculate(patId);
		
	}
	
	@PostMapping("/assess/familyName")
	public String postPatientForm(@RequestParam String familyname){

		PatientBean patient = patientProxy.getPatientByFamilyName(familyname);
		
		return diabeteAssessService.diabeteAssessCalculate(patient.getId());
	}

}
