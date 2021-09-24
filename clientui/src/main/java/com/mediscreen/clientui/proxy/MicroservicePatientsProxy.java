package com.mediscreen.clientui.proxy;

import java.util.List;

import com.mediscreen.clientui.beans.PatientBean;

public interface MicroservicePatientsProxy {

	/**
	 * returns list of all patients in database
	 * @return all patients
	 */
	public List<PatientBean> getAllPatients();
	
	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
    public PatientBean getPatient(int id);

    /**
     * updates patient with a specified id
     * @param id the patient id
     * @return updated patient
     */
    public PatientBean updatePatient(PatientBean newPatient);
    
    /**
     * creates patient.
     * @return created patient with id in database.
     */
    public PatientBean createPatient(PatientBean newPatient);

    /**
     * delete patient.
     */
    public void deletePatient(Integer id);
    
    /**
     * checks if a patient exists by firstname+lastname.
     * @return boolean true if patient exists.
     */
    public Boolean existPatient(PatientBean newPatient);
    
}
