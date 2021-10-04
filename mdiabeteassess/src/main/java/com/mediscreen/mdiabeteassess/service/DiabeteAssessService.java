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
import org.springframework.stereotype.Service;

import com.mediscreen.common.dto.NoteBean;
import com.mediscreen.common.dto.PatientBean;
import com.mediscreen.mdiabeteassess.proxy.MicroserviceNotesProxyFeign;
import com.mediscreen.mdiabeteassess.proxy.MicroservicePatientsProxyFeign;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jerome
 *
 */
@Slf4j
@Service
public class DiabeteAssessService {


	@Autowired
	private MicroservicePatientsProxyFeign patientProxy;

	@Autowired
	private MicroserviceNotesProxyFeign noteProxy;

	
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

		if( 
				( sex.equals("M") && age<30 && diabeteTriggers >= 5 ) ||
				( sex.equals("F") && age<30 && diabeteTriggers >= 7) ||
				( age>=30 && diabeteTriggers >= 8)
				) {
			diabeteAssess = "Early onset";
		}
		else if(
				( sex.equals("M") && age<30 && diabeteTriggers >= 3 ) ||
				( sex.equals("F") && age<30 && diabeteTriggers >= 4) ||
				( age>=30 && diabeteTriggers >= 6)
				) {
			diabeteAssess = "In Danger";
		}
		else if( age>=30 && diabeteTriggers >= 2 ) 
		{
			diabeteAssess = "Borderline";
		}
		else {
			diabeteAssess = "None";
		}

		return "Patient: " + patient.getGiven() + " " + patient.getFamily() +
				"(age " + age + ") diabetes assessment is: " + diabeteAssess;

	}


	private Integer diabeteTriggersCount (List<NoteBean> listNoteBean) {

		Integer counter = 0;

		// Use Matcher class of java.util.regex
		// to match the character
		Pattern p = Pattern.compile(
				"Hémoglobine A1C|Microalbumine|Taille|Poids|Fumeur|Anormal|Cholestérol|Vertige|Rechute|Réaction|Anticorps"
				//en anglais:
				+"|Hemoglobin A1C|Height|Weight|Smoker|Abnormal|Cholesterol|Dizziness|relapse|Antibodies|Reaction"
				,
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
