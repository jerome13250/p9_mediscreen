package com.mediscreen.clientui.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mediscreen.common.dto.PatientBean;

@FeignClient(name = "microservice-patients", url = "${feign.mpatient.url}")
public interface PatientsProxyFeign{

	/**
	 * returns list of all patients in database
	 * @return all patients
	 */
	@GetMapping(value = "/patients")
	public List<PatientBean> getAllPatients();
	
	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
    @GetMapping( value = "/patients/{id}")
    public PatientBean getPatient(@PathVariable int id);

    /**
     * updates patient with a specified id
     * @param id the patient id
     * @return updated patient
     */
    @PutMapping( value = "/patients")
    public PatientBean updatePatient(@RequestBody PatientBean newPatient);
    
    /**
     * creates patient.
     * @return created patient with id in database.
     */
    @PostMapping( value = "/patients/add")
    public PatientBean createPatient(@RequestBody PatientBean newPatient);

    /**
     * delete patient.
     */
    @DeleteMapping( value = "/patients/delete/{id}")
    public void deletePatient(@PathVariable Integer id);
    
    /**
     * checks if a patient exists by firstname+lastname.
     * @return boolean true if patient exists.
     */
    @PostMapping( value = "/patients/exist")
    public Boolean existPatient(@RequestBody PatientBean newPatient);
    
}
