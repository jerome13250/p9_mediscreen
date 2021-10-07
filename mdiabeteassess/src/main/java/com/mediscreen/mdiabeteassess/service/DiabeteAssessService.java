package com.mediscreen.mdiabeteassess.service;

public interface DiabeteAssessService {

	String diabeteAssessCalculateByFamilyName(String familyname);

	/**
	 * Assess the diabetes probability and return assessment value as a String
	 * @param patId the patient id
	 * @return assessment value as a String
	 */
	String diabeteAssessCalculate(Integer patId);

}