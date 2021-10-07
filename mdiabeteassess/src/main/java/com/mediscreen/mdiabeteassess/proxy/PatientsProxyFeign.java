package com.mediscreen.mdiabeteassess.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediscreen.common.dto.PatientBean;

import io.swagger.annotations.ApiOperation;

@FeignClient(name = "microservice-patients", url = "${feign.mpatient.url}")
public interface PatientsProxyFeign{

	/**
     * returns patient with a specified id
     * @param id the patient id
     * @return patient
     */
    @GetMapping( value = "/patients/{id}")
    public PatientBean getPatient(@PathVariable int id);

    /**
     * return all patients if familyName is not provided, otherwise return all patients for a specified familyName
     * 
     * @param familyname the patient family name
     * @return patient
	 * @throws PatientNotFoundException if patient is not found
     */
    @GetMapping( value = "/patients"  )
    @ApiOperation(value = "This endpoint returns all patients if familyName is not provided,"
    		+ " otherwise return all patients for a specified familyName.")
    public List<PatientBean> getPatients(@RequestParam(required = false) String familyname);
    
}
