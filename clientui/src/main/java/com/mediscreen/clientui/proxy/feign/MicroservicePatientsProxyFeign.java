package com.mediscreen.clientui.proxy.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mediscreen.clientui.beans.PatientBean;
import com.mediscreen.clientui.proxy.MicroservicePatientsProxy;

//TODO: url is static, @Value not possible, find a way to fix this ?
@FeignClient(name = "microservice-patients", url = "localhost:8081")
public interface MicroservicePatientsProxyFeign extends MicroservicePatientsProxy {

	/**
	 * returns list of all patients in database
	 * @return all patients
	 */
	@Override
	@GetMapping(value = "/patient")
	public List<PatientBean> getAllPatients();
	
	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
	@Override
    @GetMapping( value = "/patient/{id}")
    public PatientBean getPatient(@PathVariable int id);

    /**
     * updates patient with a specified id
     * @param id the patient id
     * @return updated patient
     */
	@Override
    @PutMapping( value = "/patient")
    public PatientBean updatePatient(@RequestBody PatientBean newPatient);
    
    /**
     * creates patient.
     * @return created patient with id in database.
     */
	@Override
    @PostMapping( value = "/patient/add")
    public PatientBean createPatient(@RequestBody PatientBean newPatient);

    /**
     * delete patient.
     */
	@Override
    @DeleteMapping( value = "/patient/delete/{id}")
    public void deletePatient(@PathVariable Integer id);
    
    /**
     * checks if a patient exists by firstname+lastname.
     * @return boolean true if patient exists.
     */
	@Override
    @PostMapping( value = "/patient/exist")
    public Boolean existPatient(@RequestBody PatientBean newPatient);
    
}
