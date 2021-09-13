package com.mediscreen.mpatient.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.mpatient.exception.PatientNotFoundException;
import com.mediscreen.mpatient.model.Patient;
import com.mediscreen.mpatient.repository.PatientRepository;

@RestController
public class PatientController {
	
	@Autowired
	PatientRepository patientdataRepository;
	
	/**
	 * endpoint that returns list of all patients in database
	 * @return all patients
	 * @throws PatientNotFoundException if database is empty
	 */
    @GetMapping(value = "/patient")
    public List<Patient> GetAllPatients() throws PatientNotFoundException{

        List<Patient> listePatientdata = patientdataRepository.findAll();
        if(listePatientdata.isEmpty()) throw new PatientNotFoundException("Aucun patient n'est inscrit");
        return listePatientdata;
    }
    
    /**
     * endpoint that returns patient with a specified id
     * @param id
     * @return patient data
     * @throws PatientNotFoundException if patient id does not exist in database
     */
    @GetMapping( value = "/patient/{id}")
    public Optional<Patient> getPatient(@PathVariable int id) throws PatientNotFoundException {

        Optional<Patient> patient = patientdataRepository.findById(id);
        if(!patient.isPresent())  throw new PatientNotFoundException("Le patient correspondant à l'id " + id + " n'existe pas");

        return patient;
    }
	
    /**
     * endpoint that updates patient with a specified id
     * @param id
     * @return updated patient
     * @throws PatientNotFoundException if patient id does not exist in database
     */
    @PutMapping( value = "/patient/{id}")
    public Patient updatePatient(@ModelAttribute Patient newPatient, @PathVariable Integer id) throws PatientNotFoundException {

        Optional<Patient> oldPatient = patientdataRepository.findById(id);
        if(!oldPatient.isPresent())  throw new PatientNotFoundException("Modification impossible, le patient correspondant à l'id " + id + " n'existe pas");
        
        return patientdataRepository.save(newPatient);
    }
    
    /**
     * endpoint that creates patient.
     * @return created patient with id in database.
     * @throws PatientNotFoundException if patient id does not exist in database
     */
    @PostMapping( value = "/patient/add")
    public Patient createPatient(@ModelAttribute Patient newPatient) throws PatientNotFoundException {
       
        return patientdataRepository.save(newPatient);
    }
	
}
