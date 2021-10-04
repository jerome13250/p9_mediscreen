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
import com.mediscreen.mdiabeteassess.proxy.MicroserviceNotesProxyFeign;
import com.mediscreen.mdiabeteassess.proxy.MicroservicePatientsProxyFeign;

@ExtendWith(MockitoExtension.class)
class DiabeteAssessServiceTest {

	@InjectMocks
	DiabeteAssessService diabeteAssessService;

	@Mock
	private MicroservicePatientsProxyFeign patientProxy;
	@Mock
	private MicroserviceNotesProxyFeign noteProxy;

	PatientBean patient1;
	NoteBean note1;
	NoteBean note2;
	List<NoteBean> listNoteBean;

	@BeforeEach
	void initialize() {
		patient1 = new PatientBean(1, "john", "doe", LocalDate.now().minusYears(40), "M", "address1", "111-222-333");
		note1 = new NoteBean("mongoid1", 1, "note1 with Anticorps Anticorps Anticorps Anticorps , vertige Vertige");
		note2 = new NoteBean("mongoid2", 1, "note2 with Anticorps Anticorps , Vertige Vertige Vertige");
		listNoteBean = new ArrayList<>();
		listNoteBean.add(note1);
		listNoteBean.add(note2);	
	}

	@Test
	void test_diabeteAssessCalculate() {

		//ARRANGE:
		when(patientProxy.getPatient(1)).thenReturn(patient1);
		when(noteProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);

		//ACT:
		String result = diabeteAssessService.diabeteAssessCalculate(1);

		//ASSERT:
		assertEquals("Patient: john doe(age 40) diabetes assessment is: Early onset", result);

	}


}
