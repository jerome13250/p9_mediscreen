package com.mediscreen.mpatient.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.mpatient.exception.BadRequestException;
import com.mediscreen.mpatient.exception.PatientAlreadyExistException;
import com.mediscreen.mpatient.exception.PatientNotFoundException;
import com.mediscreen.mpatient.model.Patient;
import com.mediscreen.mpatient.repository.PatientRepository;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = PatientController.class) 
class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private PatientRepository patientRepository;

	Patient patient1;
	Patient patient2;
	List<Patient> listPatient;

	@BeforeEach
	void setup() {
		patient1 = new Patient("john", "doe", LocalDate.of (1980,8,10),"M","1st street New-York","111-222-333");
		patient2 = new Patient("mike", "smith", LocalDate.of (2005,3,25),"M","Residence Palmas Miami","555-666-777");
		listPatient = new ArrayList<>();
		listPatient.add(patient1);
		listPatient.add(patient2);

	}

	@Test
	void GetAllPatients_shouldSucceed() throws Exception {
		//ARRANGE
		when(patientRepository.findAll()).thenReturn(listPatient);

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patients"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		List<Patient> listPatientResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Patient>>() {});
		assertNotNull(listPatientResult);
		assertEquals(2, listPatientResult.size());
	}

	@Test
	void GetPatient_shouldSucceed() throws Exception {
		//ARRANGE
		when(patientRepository.findById(1)).thenReturn(Optional.of(patient1));

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patients/1"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Patient patientResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Patient>() {});
		assertNotNull(patientResult);
		assertEquals("john", patientResult.getGiven());
		assertEquals("doe", patientResult.getFamily());
	}

	@Test
	void GetPatient_IsNotFoundExpected() throws Exception {
		//ARRANGE
		when(patientRepository.findById(1)).thenReturn(Optional.empty());

		//ACT+ASSERT
		mockMvc.perform(get("/patients/1"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
	}

	@Test
	void UpdatePatient_shouldSucceed() throws Exception {
		//ARRANGE
		patient1.setId(1);
		when(patientRepository.findById(1)).thenReturn(Optional.of(patient1));
		patient2.setId(1);
		when(patientRepository.save(any(Patient.class))).thenReturn(patient2);
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		MvcResult result = mockMvc.perform(
				put("/patients")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
		ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(patientRepository,times(1)).save(patientArgumentCaptor.capture());
		Patient patientCaptured = patientArgumentCaptor.getValue();
		assertEquals(1,patientCaptured.getId());
		assertEquals("mike",patientCaptured.getGiven());
		assertEquals("smith",patientCaptured.getFamily());
		assertEquals(LocalDate.of(2005,3,25),patientCaptured.getDob());
		assertEquals("M",patientCaptured.getSex());
		assertEquals("Residence Palmas Miami",patientCaptured.getAddress());
		assertEquals("555-666-777",patientCaptured.getPhone());

		Patient patientReturned = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Patient>() {});
		assertNotNull(patientReturned);
		assertEquals("mike", patientReturned.getGiven());
		assertEquals("smith", patientReturned.getFamily());
		assertEquals(LocalDate.of(2005,3,25),patientReturned.getDob());
		assertEquals("M",patientReturned.getSex());
		assertEquals("Residence Palmas Miami",patientReturned.getAddress());
		assertEquals("555-666-777",patientReturned.getPhone());
	}

	@Test
	void PutPatient_IsBadRequestExpected() throws Exception {
		//ARRANGE
		patient1.setId(null);
		String jsonContent = objectMapper.writeValueAsString(patient1);

		//ACT+ASSERT
		mockMvc.perform(put("/patients")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isBadRequest())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
	}
	
	@Test
	void PutPatient_IsNotFoundExpected() throws Exception {
		//ARRANGE
		patient1.setId(1);
		String jsonContent = objectMapper.writeValueAsString(patient1);

		when(patientRepository.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT
		mockMvc.perform(put("/patients")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
	}

	@Test
	void CreatePatient_shouldSucceed() throws Exception {
		//ARRANGE
		when(patientRepository.existsByFirstNameLastname("mike", "smith")).thenReturn(Boolean.FALSE);
		patient2.setId(1);
		when(patientRepository.save(any(Patient.class))).thenReturn(patient2);
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		MvcResult result = 
				mockMvc.perform(
						post("/patients/add")
						.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
						)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
		ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);
		verify(patientRepository,times(1)).save(patientArgumentCaptor.capture());
		Patient patientCaptured = patientArgumentCaptor.getValue();

		assertEquals("mike",patientCaptured.getGiven());
		assertEquals("smith",patientCaptured.getFamily());
		assertEquals(LocalDate.of(2005,3,25),patientCaptured.getDob());
		assertEquals("M",patientCaptured.getSex());
		assertEquals("Residence Palmas Miami",patientCaptured.getAddress());
		assertEquals("555-666-777",patientCaptured.getPhone());

		Patient patientReturned = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Patient>() {});
		assertNotNull(patientReturned);
		assertEquals(1,patientReturned.getId());
		assertEquals("mike", patientReturned.getGiven());
		assertEquals("smith", patientReturned.getFamily());
	}

	@Test
	void CreatePatient_AlreadyExistExpected() throws Exception {
		//ARRANGE
		when(patientRepository.existsByFirstNameLastname("mike", "smith")).thenReturn(Boolean.TRUE);
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		mockMvc.perform(post("/patients/add")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isConflict())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientAlreadyExistException));
	}

	@Test
	void DeletePatient_shouldSucceed() throws Exception {
		//ARRANGE
		when(patientRepository.existsById(1)).thenReturn(Boolean.TRUE);

		//ACT+ASSERT
		mockMvc.perform(delete("/patients/delete/1"));

		//check patient update:
		verify(patientRepository,times(1)).deleteById(1);
	}

	@Test
	void DeletePatient_DoesntExistExpected() throws Exception {
		//ARRANGE
		when(patientRepository.existsById(1)).thenReturn(Boolean.FALSE);

		//ACT+ASSERT
		mockMvc.perform(delete("/patients/delete/1"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));

		//check patient update:
		verify(patientRepository,never()).deleteById(1);
	}

	@Test
	void existPatientByFirstNameLastname() throws Exception {
		//ARRANGE
		when(patientRepository.existsByFirstNameLastname("mike", "smith")).thenReturn(Boolean.FALSE);
		patient2.setId(1);
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		MvcResult result = 
				mockMvc.perform(
						post("/patients/exist")
						.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
						)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
		Boolean booleanResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Boolean>() {});
		assertEquals(Boolean.FALSE,booleanResult);
	}

	@Test
	void getPatientByFamilyName_shouldSucceed() throws Exception {
		//ARRANGE
		List<Patient> listPatient = new ArrayList<>();
		listPatient.add(patient1);
		when(patientRepository.findByFamily("doe")).thenReturn(listPatient);

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patients?familyname=doe"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		List<Patient> listpatientResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Patient>>() {});
		assertNotNull(listpatientResult);
		assertEquals("john", listpatientResult.get(0).getGiven());
		assertEquals("doe", listpatientResult.get(0).getFamily());
	}

	@Test
	void getPatientByFamilyName_IsNotFoundExpected() throws Exception {
		//ARRANGE
		when(patientRepository.findByFamily("doe")).thenReturn(new ArrayList<>());

		//ACT+ASSERT
		mockMvc.perform(get("/patients?familyname=doe"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
	}

	
}
