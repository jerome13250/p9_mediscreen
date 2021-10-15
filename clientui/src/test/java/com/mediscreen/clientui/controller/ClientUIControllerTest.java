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

import com.mediscreen.common.dto.NoteBean;
import com.mediscreen.common.dto.PatientBean;
import com.mediscreen.clientui.proxy.NotesProxyFeign;
import com.mediscreen.clientui.proxy.PatientsProxyFeign;

@WebMvcTest(controllers = ClientUIController.class) 
class ClientUIControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PatientsProxyFeign patientsProxy;
	@MockBean
	private NotesProxyFeign notesProxy;

	PatientBean patientBean1;
	PatientBean patientBean2;
	List<PatientBean> listPatientsBean;
	NoteBean noteBean1;
	NoteBean noteBean2;
	List<NoteBean> listNoteBean;

	@BeforeEach
	void setup() {
		patientBean1 = new PatientBean(1, "firstname1", "lastname1", LocalDate.of(2000, 01, 10),"M", "1st street New York" , "111-222-333" );
		patientBean2 = new PatientBean(2, "firstname2", "lastname2", LocalDate.of(1990, 12, 30),"F", "2nd street Miami" , "444-555-999" );
		listPatientsBean = new ArrayList<>();
		listPatientsBean.add(patientBean1);
		listPatientsBean.add(patientBean2);
		
		noteBean1 = new NoteBean("mongoid_1", 1, "note1");
		noteBean2 = new NoteBean("mongoid_2", 1, "note2");
		listNoteBean = new ArrayList<>();
		listNoteBean.add(noteBean1);
		listNoteBean.add(noteBean2);
		
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

		//check user delete:
		verify(patientsProxy,times(1)).deletePatient(1);
		//check notes delete:
		verify(notesProxy,times(1)).deleteAllNotesByPatientId(1);
		
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
	void PostPatientForm_UpdatePatient() throws Exception {
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
		.andExpect(redirectedUrl("/patients/1"))
		;

		//check user create:
		ArgumentCaptor<PatientBean> patientBeanArgumentCaptor = ArgumentCaptor.forClass(PatientBean.class);
		verify(patientsProxy,times(1)).updatePatient(patientBeanArgumentCaptor.capture());
		PatientBean patientBeanCaptured = patientBeanArgumentCaptor.getValue();
		assertEquals(patientBean1,patientBeanCaptured);
		assertEquals(1,patientBeanCaptured.getId());

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
		.andExpect(model().attributeErrorCount("patient", 7))
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
	
	@Test
	void GetPatientNotes() throws Exception {
		//ARRANGE:
		when(patientsProxy.getPatient(1)).thenReturn(patientBean1);
		when(notesProxy.getListOfNotesByPatientId(1)).thenReturn(listNoteBean);

		//ACT+ASSERT:
		mockMvc.perform(get("/patients/1"))
		.andExpect(status().isOk())
		.andExpect(view().name("patientNotes"))
		.andExpect(model().size(2))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attribute("patient", patientBean1))
		.andExpect(model().attributeExists("listNotes"))
		.andExpect(model().attribute("listNotes", listNoteBean))
		;
	}
	
	@Test
	void GetNoteForm_CreateNewNote() throws Exception {
		//ARRANGE:
		NoteBean expectedNoteBean = new NoteBean(null,1,null);
		//ACT+ASSERT:
		mockMvc.perform(get("/noteForm?patId=1"))
		.andExpect(status().isOk())
		.andExpect(view().name("noteForm"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("note"))
		.andExpect(model().attribute("note", expectedNoteBean)) //must be equal to expectedNoteBean patId=1
		;

		//mongo id is unset => note creation => check no existing note is loaded from db
		verify(notesProxy,never()).getNote(any(String.class));

	}
	
	@Test
	void GetNoteForm_UpdateExistingPatient() throws Exception {
		//ARRANGE:
		NoteBean expectedNoteBean = new NoteBean("mongoid",1,"existing note text");
		when(notesProxy.getNote("mongoid")).thenReturn(expectedNoteBean);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/noteForm?patId=1&id=mongoid"))
		.andExpect(status().isOk())
		.andExpect(view().name("noteForm"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("note"))
		.andExpect(model().attribute("note", expectedNoteBean)) //must be equal to expectedNoteBean
		;

	}
	
	@Test
	void PostNoteForm_UpdateNote() throws Exception {
		//ARRANGE

		//ACT+ASSERT:
		//id set => note update
		mockMvc.perform(post("/noteForm")
				.param("id", "mongoid_1")
				.param("patId", "1")
				.param("noteText", "note1")
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients/1"))
		;

		//check note update:
		ArgumentCaptor<NoteBean> noteBeanArgumentCaptor = ArgumentCaptor.forClass(NoteBean.class);
		verify(notesProxy,times(1)).updateNote(noteBeanArgumentCaptor.capture());
		NoteBean noteBeanCaptured = noteBeanArgumentCaptor.getValue();
		assertEquals(noteBean1,noteBeanCaptured);
		assertEquals("mongoid_1",noteBeanCaptured.getId());

	}

	@Test
	void PostNoteForm_CreateNewNote() throws Exception {
		//ARRANGE

		//ACT+ASSERT:
		//mongoid not set => note create
		mockMvc.perform(post("/noteForm")
				.param("patId", "1")
				.param("noteText", "note1")
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients/1"))
		;

		//check note update:
		ArgumentCaptor<NoteBean> noteBeanArgumentCaptor = ArgumentCaptor.forClass(NoteBean.class);
		verify(notesProxy,times(1)).createNote(noteBeanArgumentCaptor.capture());
		NoteBean noteBeanCaptured = noteBeanArgumentCaptor.getValue();
		assertEquals(noteBean1,noteBeanCaptured);
		assertEquals(null,noteBeanCaptured.getId());

	}

	@Test
	void PostNoteForm_bindingErrors() throws Exception {
		//ARRANGE

		//ACT+ASSERT:
		//no id set => new patient creation
		mockMvc.perform(post("/noteForm")
				)
		.andExpect(status().isOk())//stays on patientForm
		.andExpect(view().name("noteForm"))
		.andExpect(model().attributeErrorCount("note", 2))
		;

	}

	@Test
	void PostNoteDelete() throws Exception {
		//ARRANGE

		//ACT
		mockMvc.perform(post("/patients/1/notes/delete/mongoid1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/patients/1"))
		;

		//check note delete:
		verify(notesProxy,times(1)).deleteNote("mongoid1");

		
	}
}
