package com.mediscreen.clientui.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediscreen.clientui.beans.PatientBean;
import com.mediscreen.clientui.proxy.MicroservicePatientsProxy;

@Controller
public class ClientUIController {

	@Autowired
	private MicroservicePatientsProxy patientProxy;

	@GetMapping("/")
	public String accueil(Model model){
		return "accueil";
	}

	@GetMapping("/patients")
	public String patients(Model model){

		List<PatientBean> patients =  patientProxy.getAllPatients();
		model.addAttribute("patients", patients);
		return "patients";
	}

	@PostMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable Integer id){

        patientProxy.deletePatient(id);
        return "redirect:/patients";
    }

	@GetMapping("/patientForm")
	public String showPatientForm(@RequestParam(required=false) Integer id, Model model){

		//id is unset: create patient
		if (id==null) {
			model.addAttribute("patient", new PatientBean());
		}
		//id is set: update patient
		else {
			model.addAttribute("patient", patientProxy.getPatient(id));
		}
		return "patientForm";
	}

	@PostMapping("/patientForm")
	public String postPatientForm(@Valid @ModelAttribute("patient") PatientBean patient, BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {        	
			return "patientForm";
		}
		
		//cross-record validation:
		if (patient.getId()==null && patientProxy.existPatient(patient).equals(Boolean.TRUE)) {
			bindingResult.rejectValue("given", "", "Utilisateur déja inscrit!");
			bindingResult.rejectValue("family", "", "Utilisateur déja inscrit!");
			return "patientForm";
		}
		
		if (patient.getId() == null) {
			patientProxy.createPatient(patient);
		}
		else {
			patientProxy.updatePatient(patient, patient.getId());
		}

		return "redirect:/patients";
	}
	
}
