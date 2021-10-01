package com.mediscreen.mdiabeteassess.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediscreen.common.dto.PatientBean;

@FeignClient(name = "microservice-patients", url = "${feign.mpatient.url}")
public interface MicroservicePatientsProxyFeign{

	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
    @GetMapping( value = "/patients/{id}")
    public PatientBean getPatient(@PathVariable int id);

	/**
     * returns patient with a specified familyName
     * note: if familyName is not unique in database it returns the first one.
     * 
     * @param id the patient id
     * @return patient
     */
    @GetMapping( value = "/patients")
    public PatientBean getPatientByFamilyName(@RequestParam String familyname);
    
}
