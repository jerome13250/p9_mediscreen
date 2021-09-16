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

@FeignClient(name = "microservice-patients", url = "localhost:8081")
public interface MicroservicePatientsProxy {

	/**
	 * returns list of all patients in database
	 * @return all patients
	 */
	@GetMapping(value = "/patient")
	public List<PatientBean> GetAllPatients();
	
	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
    @GetMapping( value = "/patient/{id}")
    public PatientBean getPatient(@PathVariable int id);

    /**
     * updates patient with a specified id
     * @param id the patient id
     * @return updated patient
     */
    @PutMapping( value = "/patient/{id}")
    public PatientBean updatePatient(@RequestBody PatientBean newPatient, @PathVariable Integer id);
    
    /**
     * creates patient.
     * @return created patient with id in database.
     */
    @PostMapping( value = "/patient/add")
    public PatientBean createPatient(@RequestBody PatientBean newPatient);

    /**
     * delete patient.
     */
    @DeleteMapping( value = "/patient/delete/{id}")
    public void deletePatient(@PathVariable Integer id);
    
    /**
     * checks if a patient exists by firstname+lastname.
     * @return boolean true if patient exists.
     */
    @PostMapping( value = "/patient/exist")
    public Boolean existPatient(@RequestBody PatientBean newPatient);
    
}
