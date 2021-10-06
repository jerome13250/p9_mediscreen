/**
 * 
 */
package com.mediscreen.mdiabeteassess.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mediscreen.common.dto.NoteBean;
import com.mediscreen.common.dto.PatientBean;
import com.mediscreen.mdiabeteassess.proxy.NotesProxyFeign;
import com.mediscreen.mdiabeteassess.proxy.PatientsProxyFeign;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jerome
 *
 */
@Slf4j
@Service
public class DiabeteAssessService {


	@Autowired
	private PatientsProxyFeign patientProxy;

	@Autowired
	private NotesProxyFeign noteProxy;

	@Value("${diabete.assess.regexp}")
	String regexp;


	public String diabeteAssessCalculateByFamilyName(String familyname) {

		PatientBean patient = patientProxy.getPatientByFamilyName(familyname);

		return diabeteAssessCalculate(patient.getId());
	}

	/**
	 * Assess the diabetes probability and return assessment value as a String
	 * @param patId the patient id
	 * @return assessment value as a String
	 */
	public String diabeteAssessCalculate(Integer patId) {

		String diabeteAssess;

		PatientBean patient = patientProxy.getPatient(patId);
		List<NoteBean> listNoteBean = noteProxy.getListOfNotesByPatientId(patId);

		Integer diabeteTriggers = diabeteTriggersCount(listNoteBean);
		String sex = patient.getSex();
		Integer age = Period.between(patient.getDob(), LocalDate.now()).getYears();

		if( age<30 ) {
			if ( sex.equals("M") ) {
				if (diabeteTriggers >= 5) {
					diabeteAssess = "Early onset";
				}
				else if ( diabeteTriggers >= 3 ) {
					diabeteAssess = "In Danger";
				}
				else {
					diabeteAssess = "None";
				}
			}
			else { //sex=Female
				if (diabeteTriggers >= 7) {
					diabeteAssess = "Early onset";
				}
				else if ( diabeteTriggers >= 4 ) {
					diabeteAssess = "In Danger";
				}
				else {
					diabeteAssess = "None";
				}
			}
		}
		else { //age>=30
			if (diabeteTriggers >= 8) {
				diabeteAssess = "Early onset";
			}
			else if ( diabeteTriggers >= 6 ) {
				diabeteAssess = "In Danger";
			}
			else if ( diabeteTriggers >= 2 ) {
				diabeteAssess = "Borderline";
			}
			else {
				diabeteAssess = "None";
			}
		}



		return "Patient: " + patient.getGiven() + " " + patient.getFamily() +
				" (age " + age + ") diabetes assessment is: " + diabeteAssess;

	}


	private Integer diabeteTriggersCount (List<NoteBean> listNoteBean) {


		Integer counter = 0;

		// Use Matcher class of java.util.regex
		// to match the character
		Pattern p = Pattern.compile(
				regexp,
				Pattern.CASE_INSENSITIVE);

		for (NoteBean note : listNoteBean) {
			log.info(note.getNote());

			Matcher m = p.matcher(note.getNote());
			int res = 0;

			// for every presence increment the counter res by 1
			while (m.find()) {
				res++;
			}
			counter += res;

		}
		return counter;

	}

}
