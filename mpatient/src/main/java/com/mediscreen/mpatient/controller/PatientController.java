package com.mediscreen.mpatient.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.mpatient.exception.BadRequestException;
import com.mediscreen.mpatient.exception.PatientAlreadyExistException;
import com.mediscreen.mpatient.exception.PatientNotFoundException;
import com.mediscreen.mpatient.model.Patient;
import com.mediscreen.mpatient.repository.PatientRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class PatientController {

	@Autowired
	PatientRepository patientRepository;

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
    public List<Patient> getPatients(
    		@ApiParam(
					value = "Family name"
					)
    		@RequestParam(required = false) String familyname) throws PatientNotFoundException {
    	
    	//familyName not provided, return all patients
    	if (familyname == null) {
    		List<Patient> listePatient = patientRepository.findAll();
    		return listePatient;
    	}
    	else {
    		List<Patient> listePatient = patientRepository.findByFamily(familyname);
    		if(listePatient.isEmpty())  throw new PatientNotFoundException("Le patient correspondant au nom de famille " + familyname + " n'existe pas");
    		return listePatient;
    	}
    }
    
	/**
	 * endpoint that returns patient with a specified id
	 * @param id the patient id
	 * @return patient data
	 * @throws PatientNotFoundException if patient id does not exist in database
	 */
	@ApiOperation(value = "This endpoint returns a patient from its id.")
	@GetMapping( value = "/patients/{id}")
	public Optional<Patient> getPatient(
			@ApiParam(
					value = "Patient id",
					example = "1")
			@PathVariable int id) 
					throws PatientNotFoundException {

		Optional<Patient> patient = patientRepository.findById(id);
		if(!patient.isPresent())  throw new PatientNotFoundException("Le patient correspondant à l'id " + id + " n'existe pas");

		return patient;
	}
    
	/**
	 * endpoint that updates patient
	 * <p>
	 * note: patient id must be provided in newPatient object.
	 * </p>
	 * @param updatedPatient the patient to update
	 * @return the returned updated patient from database
	 * @throws PatientNotFoundException if patient id does not exist in database
	 * @throws BadRequestException if patient id is null
	 */
	@ApiOperation(value = "This endpoint updates a patient.")
	@PutMapping( value = "/patients")
	public Patient updatePatient(
			@ApiParam(
					value = "Patient object in json format"
					)
			@Valid @RequestBody Patient updatedPatient)
					throws PatientNotFoundException, BadRequestException {

		//id must be set in updatedPatient
		if(updatedPatient.getId()==null) throw new BadRequestException("Patient id can not be null!");

		Optional<Patient> oldPatient = patientRepository.findById(updatedPatient.getId());
		if(!oldPatient.isPresent())  throw new PatientNotFoundException("Modification impossible, le patient correspondant à l'id " + updatedPatient.getId() + " n'existe pas");

		return patientRepository.save(updatedPatient);
	}

	/**
	 * endpoint that creates patient.
	 * <p>
	 * note: whatever patient id value provided, it will be set to null to create a new patient.
	 * </p>
	 * @param newPatient the new patient
	 * @return created patient with id in database.
	 * @throws PatientAlreadyExistException if patient already exists in database, based on firstname+lastname
	 */
	@ApiOperation(value = "This endpoint creates a patient.")
	@PostMapping( value = "/patients/add")
	public Patient createPatient(
			@ApiParam(
					value = "Patient object in json format"
					)
			@Valid @RequestBody Patient newPatient) 
					throws PatientAlreadyExistException {

		Boolean existPatient = patientRepository.existsByFirstNameLastname(newPatient.getGiven(), newPatient.getFamily());
		if(existPatient.equals(Boolean.TRUE))  throw new PatientAlreadyExistException("Ajout impossible, le patient " + newPatient.getGiven() + " " + newPatient.getFamily() + " existe deja");

		//we create a new patient. To avoid id collision we set it to null to let database create id.
		newPatient.setId(null);

		return patientRepository.save(newPatient);
	} 

	/**
	 * endpoint that deletes patient with a specified id
	 * @param id the patient id
	 * @throws PatientNotFoundException if patient id does not exist in database
	 */
	@ApiOperation(value = "This endpoint deletes a patient.")
	@DeleteMapping( value = "/patients/delete/{id}")
	public void deletePatient(
			@ApiParam(
					value = "Patient id",
					example = "1")
			@PathVariable Integer id
			) throws PatientNotFoundException {

		if(!patientRepository.existsById(id)) throw new PatientNotFoundException("Suppression impossible, le patient correspondant à l'id " + id + " n'existe pas");
		patientRepository.deleteById(id);
	}

	/**
	 * endpoint that checks if a patient exists by firstname+lastname.
	 * @param patient to check existence
	 * @return boolean true if patient exists.
	 */
	@ApiOperation(value = "This endpoint checks if a patient exist by firstname and lastname.")
	@PostMapping( value = "/patients/exist")
	public Boolean existPatient(
			@ApiParam(
					value = "Patient object in json format"
					)
			@Valid @RequestBody Patient patient
			) {

		Boolean existPatient = patientRepository.existsByFirstNameLastname(patient.getGiven(), patient.getFamily());

		return existPatient;
	} 
	

}


