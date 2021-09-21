package com.mediscreen.clientui.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public PatientBean updatePatient(PatientBean newPatient,Integer id);
    
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
