package com.mediscreen.mdiabeteassess.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediscreen.common.dto.NoteBean;
import com.mediscreen.common.dto.PatientBean;
import com.mediscreen.mdiabeteassess.proxy.NotesProxyFeign;
import com.mediscreen.mdiabeteassess.proxy.PatientsProxyFeign;

@ExtendWith(MockitoExtension.class)
class DiabeteAssessServiceImplTest {

	@InjectMocks
	DiabeteAssessServiceImpl diabeteAssessServiceImpl;

	@Mock
	private PatientsProxyFeign patientProxy;
	@Mock
	private NotesProxyFeign noteProxy;

	PatientBean patient1;
	NoteBean note1;
	NoteBean note2;
	List<NoteBean> listNoteBean;

	@BeforeEach
	void initialize() {
		diabeteAssessServiceImpl.regexp = "Anticorps|Vertige";
		listNoteBean = new ArrayList<>();
		
	}


	@Test
	void test_diabeteAssess_MaleInf30Years5Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(20), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps Anticorps");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps vertige");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 20) diabetes assessment is: Early onset", result);
	}

	@Test
	void test_diabeteAssess_MaleInf30Years3Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(20), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps");
		note2 = new NoteBean("mongoid2", 1, "note2 with Vertige");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 20) diabetes assessment is: In Danger", result);
	}

	@Test
	void test_diabeteAssess_MaleInf30Years2Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(29), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with none");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 29) diabetes assessment is: None", result);
	}

	@Test
	void test_diabeteAssess_FemaleInf30Years7Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(20), "F", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps Anticorps vertige Vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Vertige");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 20) diabetes assessment is: Early onset", result);
	}

	@Test
	void test_diabeteAssess_FemaleInf30Years4Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(20), "F", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Vertige");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 20) diabetes assessment is: In Danger", result);
	}

	@Test
	void test_diabeteAssess_FemaleInf30Years3Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(29), "F", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 29) diabetes assessment is: None", result);
	}

	@Test
	void test_diabeteAssess_Sup30Years8Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(31), "F", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps Anticorps Anticorps , vertige Vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Anticorps");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 31) diabetes assessment is: Early onset", result);
	}

	@Test
	void test_diabeteAssess_Sup30Years6Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(31), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps  vertige Vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Anticorps");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 31) diabetes assessment is: In Danger", result);
	}

	@Test
	void test_diabeteAssess_Sup30Years2Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(31), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with none");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);		

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 31) diabetes assessment is: Borderline", result);
	}

	@Test
	void test_diabeteAssess_Sup30Years1Triggers() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(31), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with none");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);	

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 31) diabetes assessment is: None", result);
	}

	@Test
	void test_diabeteAssessCalculateByFamilyName() {
		//ARRANGE:
		patient1 = new PatientBean(1, "firstname", "lastname", LocalDate.now().minusYears(20), "M", "address1", "111-222-333");
		List<PatientBean> listPatientBean = new ArrayList<>();
		listPatientBean.add(patient1);
		when(patientProxy.getPatients("lastname")).thenReturn(listPatientBean);
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps Anticorps Anticorps , vertige Vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Anticorps");
		listNoteBean.add(note1);
		listNoteBean.add(note2);
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);

		//ACT:
		String result = diabeteAssessServiceImpl.diabeteAssessCalculateByFamilyName("lastname");

		//ASSERT:
		assertEquals("Patient: firstname lastname (age 20) diabetes assessment is: Early onset", result);

	}

}
