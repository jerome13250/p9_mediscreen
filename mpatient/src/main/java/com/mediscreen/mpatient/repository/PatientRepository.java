package com.mediscreen.mpatient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediscreen.mpatient.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
	
	/**
	 * This query checks if a patient firstname+lastname already exists.
	 * @param firstname the patient firstname
	 * @param lastname the patient lastname
	 * @return true if already exists, otherwise false.
	 */
	@Query("SELECT CASE "
			+ "WHEN COUNT(p) > 0 THEN true"
			+ " ELSE false END "
			+ "FROM Patient p "
			+ "WHERE p.given = :firstname AND p.family = :lastname")
    public Boolean existsByFirstNameLastname(@Param("firstname") String firstname, @Param("lastname") String lastname);
	
	/**
	 * derived query that returns patients found with family name
	 * @return List of patients if found, empty list otherwise.
	 */
	public List<Patient> findByFamily(String family);
}
