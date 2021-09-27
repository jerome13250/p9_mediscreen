package com.mediscreen.clientui.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediscreen.clientui.beans.NoteBean;
import com.mediscreen.clientui.beans.PatientBean;
import com.mediscreen.clientui.proxy.MicroserviceNotesProxyFeign;
import com.mediscreen.clientui.proxy.MicroservicePatientsProxyFeign;

@Controller
public class ClientUIController {

	@Autowired
	private MicroservicePatientsProxyFeign patientProxy;

	@Autowired
	private MicroserviceNotesProxyFeign noteProxy;

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

	//Use POST since browsers do not support PUT and DELETE via form submission
	//https://code.i-harness.com/en/q/1007044
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
			patientProxy.updatePatient(patient);
		}

		return "redirect:/patients";
	}

	@GetMapping("/patients/note/{id}")
	public String patientNotes(@PathVariable Integer id, Model model){

		PatientBean patient =  patientProxy.getPatient(id);
		model.addAttribute("patient", patient);

		List<NoteBean> listNotes = noteProxy.getListOfNotesByPatientId(id);
		model.addAttribute("listNotes", listNotes);

		return "patientNotes";
	}

	@GetMapping("/noteForm")
	public String showNoteForm(@RequestParam(required=false) String id, @RequestParam Integer patId, Model model){

		//id is unset: create note
		if (id==null) {
			NoteBean newNoteBean = new NoteBean();
			newNoteBean.setPatId(patId);
			model.addAttribute("note", newNoteBean);
		}
		//id is set: update note
		else {
			model.addAttribute("note", noteProxy.getNote(id));
		}
		return "noteForm";
	}

	@PostMapping("/noteForm")
	public String postNoteForm(@Valid @ModelAttribute("note") NoteBean note, BindingResult bindingResult){

		if (bindingResult.hasErrors()) {        	
			return "noteForm";
		}

		//note: no cross-record validation for unicity, each patientid can have multiple notes

		if (note.getId() == null || note.getId().equals("")) {
			noteProxy.createNote(note);
		}
		else {
			noteProxy.updateNote(note);
		}

		return "redirect:/patients/note/" + note.getPatId();
	}

	//Use POST since browsers do not support PUT and DELETE via form submission
	//https://code.i-harness.com/en/q/1007044
	@PostMapping("/notes/delete/{id}")
	public String deleteNote(@PathVariable String id){

		noteProxy.deleteNote(id);
		return "redirect:/patients"; //FIXME: pas la bonne redirection
	}
}
