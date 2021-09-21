package com.mediscreen.clientui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mediscreen.clientui.beans.PatientBean;
import com.mediscreen.clientui.proxy.MicroservicePatientsProxy;

@WebMvcTest(controllers = ClientUIController.class) 
class ClientUIControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MicroservicePatientsProxy patientsProxy;

	PatientBean patientBean1;
	PatientBean patientBean2;
	List<PatientBean> listPatientsBean;

	@BeforeEach
	void setup() {
		patientBean1 = new PatientBean(1, "firstname1", "lastname1", LocalDate.of(2000, 01, 10),"M", "1st street New York" , "111-222-333" );
		patientBean2 = new PatientBean(2, "firstname2", "lastname2", LocalDate.of(1990, 12, 30),"F", "2nd street Miami" , "444-555-999" );
		listPatientsBean = new ArrayList<>();
		listPatientsBean.add(patientBean1);
		listPatientsBean.add(patientBean2);
	}

	@Test
	void accueilTest() throws Exception {

		mockMvc.perform(get("/"))
		.andExpect(status().isOk());
	}

	@Test
	void GetPatientsTest() throws Exception {
		//ARRANGE
		when(patientsProxy.getAllPatients()).thenReturn(listPatientsBean);

		//ACT+ASSERT:
		mockMvc.perform(get("/patients"))
		.andExpect(status().isOk())
		.andExpect(view().name("patients"))
		;
	}

	@Test
	void PostPatientDelete() throws Exception {
		//ARRANGE

		//ACT
		mockMvc.perform(post("/patients/delete/1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients"))
		;

		//check user update:
		verify(patientsProxy,times(1)).deletePatient(1);
	}

	@Test
	void GetPatientForm_UpdateExistingPatient() throws Exception {
		//ARRANGE:
		when(patientsProxy.getPatient(1)).thenReturn(patientBean1);

		//ACT+ASSERT:
		mockMvc.perform(get("/patientForm?id=1"))
		.andExpect(status().isOk())
		.andExpect(view().name("patientForm"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attribute("patient", patientBean1))
		;

		//id is unset => patient creation => check no existing patient is loaded from db
		verify(patientsProxy,times(1)).getPatient(1);

	}

	@Test
	void GetPatientForm_CreateNewPatient() throws Exception {

		//ACT+ASSERT:
		mockMvc.perform(get("/patientForm"))
		.andExpect(status().isOk())
		.andExpect(view().name("patientForm"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attribute("patient", new PatientBean())) //must be equal to empty PatientBean
		;

		//id is unset => patient creation => check no existing patient is loaded from db
		verify(patientsProxy,never()).getPatient(any(Integer.class));

	}

	@Test
	void PostPatientForm_UpdateNewPatient() throws Exception {
		//ARRANGE

		//ACT+ASSERT:
		//id set => patient update
		mockMvc.perform(post("/patientForm")
				.param("id", "1")
				.param("given", "firstname1")
				.param("family", "lastname1")
				.param("dob", "2000-01-10")
				.param("sex", "M")
				.param("address", "1st street New York")
				.param("phone", "111-222-333")
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients"))
		;

		//check user create:
		ArgumentCaptor<PatientBean> patientBeanArgumentCaptor = ArgumentCaptor.forClass(PatientBean.class);
		ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(patientsProxy,times(1)).updatePatient(patientBeanArgumentCaptor.capture(),integerArgumentCaptor.capture());
		PatientBean patientBeanCaptured = patientBeanArgumentCaptor.getValue();
		Integer integerCaptured = integerArgumentCaptor.getValue();
		assertEquals(patientBean1,patientBeanCaptured);
		assertEquals(1,patientBeanCaptured.getId());
		assertEquals(1,integerCaptured);

	}

	@Test
	void PostPatientForm_CreateNewPatient() throws Exception {
		//ARRANGE
		when(patientsProxy.existPatient(any(PatientBean.class))).thenReturn(Boolean.FALSE);

		//ACT+ASSERT:
		//no id set => new patient creation
		mockMvc.perform(post("/patientForm")
				.param("given", "firstname1")
				.param("family", "lastname1")
				.param("dob", "2000-01-10")
				.param("sex", "M")
				.param("address", "1st street New York")
				.param("phone", "111-222-333")
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients"))
		;

		//check user create:
		ArgumentCaptor<PatientBean> patientBeanArgumentCaptor = ArgumentCaptor.forClass(PatientBean.class);
		verify(patientsProxy,times(1)).createPatient(patientBeanArgumentCaptor.capture());
		PatientBean patientBeanCaptured = patientBeanArgumentCaptor.getValue();
		assertEquals(patientBean1,patientBeanCaptured);

	}

	@Test
	void PostPatientForm_bindingErrors() throws Exception {
		//ARRANGE

		//ACT+ASSERT:
		//no id set => new patient creation
		mockMvc.perform(post("/patientForm")
				.param("given", "")
				.param("family", "")
				.param("dob", "")
				.param("sex", "")
				.param("address", "")
				.param("phone", "")
				)
		.andExpect(status().isOk())//stays on patientForm
		.andExpect(view().name("patientForm"))
		.andExpect(model().attributeErrorCount("patient", 5))
		;

	}

	@Test
	void PostPatientForm_utilisateurDejaInscrit() throws Exception {
		//ARRANGE
		when(patientsProxy.existPatient(any(PatientBean.class))).thenReturn(Boolean.TRUE);

		//ACT+ASSERT:
		//no id set => new patient creation
		mockMvc.perform(post("/patientForm")
				.param("given", "firstname1")
				.param("family", "lastname1")
				.param("dob", "2000-01-10")
				.param("sex", "M")
				.param("address", "1st street New York")
				.param("phone", "111-222-333")
				)
		.andExpect(status().isOk())//stays on patientForm
		.andExpect(view().name("patientForm"))
		.andExpect(model().attributeErrorCount("patient", 2))
		;

		//check user create:
		verify(patientsProxy,never()).createPatient(any(PatientBean.class));
	}

}
