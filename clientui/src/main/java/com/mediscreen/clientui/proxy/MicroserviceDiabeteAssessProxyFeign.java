package com.mediscreen.clientui.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-diabeteassess", url = "${feign.diabeteassess.url}")
public interface MicroserviceDiabeteAssessProxyFeign{

	/**
	 * Assess diabete for a patient by id
	 * @param patId the patient id
	 * @return the assessment for diabete
	 */
	@PostMapping("/assess/id")
	public String postAssessById(@RequestParam Integer patId);
	
	/**
	 * Assess diabete for a patient by family name.
	 * @param familyname the patient family name
	 * @return the assessment for diabete
	 */
	@PostMapping("/assess/familyName")
	public String postPatientForm(@RequestParam String familyname);
	
}
