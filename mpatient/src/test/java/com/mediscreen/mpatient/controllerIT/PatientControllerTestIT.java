package com.mediscreen.mpatient.controllerIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.mpatient.exception.PatientAlreadyExistException;
import com.mediscreen.mpatient.exception.PatientNotFoundException;
import com.mediscreen.mpatient.model.Patient;
import com.mediscreen.mpatient.repository.PatientRepository;

@SpringBootTest
@AutoConfigureMockMvc
//Merges properties from the default file and the profiled file: application–test.properties file in the src/test/resources
@ActiveProfiles("test")
//@Transactional creates a new transaction that is by default automatically ROLLED BACK after test completion.
@Transactional
//Populate H2 test database: 
//note that database and schema is created by spring.jpa.hibernate.ddl-auto=create
@Sql(statements = { 
	    "INSERT INTO `patient` (`id`, `firstname`, `lastname`, `birthdate`, `sex`, `address`, `phone`) VALUES\r\n"
	    + "	(1, 'john', 'doe', '1990-09-10', 'M', '1st street New-York', '222333444555'),\r\n"
	    + "	(2, 'jane', 'doe', '1995-05-24', 'F', '1st street New-York', '222333444555'),\r\n"
	    + "	(3, 'michael', 'knight', '1999-12-25', 'M', 'Palmas Residence Miami', '111222333444'),\r\n"
	    + "	(4, 'eva', 'johnson', '2003-09-23', 'M', '23rd street Seattle', '999888777666');"
	    }
	)

class PatientControllerTestIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	PatientRepository patientRepository;

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

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patient"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		List<Patient> listPatientResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Patient>>() {});
		assertNotNull(listPatientResult);
		assertEquals(4, listPatientResult.size());
	}

	@Test
	void GetPatient_shouldSucceed() throws Exception {

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patient/1"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Patient patientResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Patient>() {});
		assertNotNull(patientResult);
		assertEquals("john", patientResult.getGiven());
		assertEquals("doe", patientResult.getFamily());
	}

	@Test
	void GetPatient_IsNotFoundExpected() throws Exception {

		//ACT+ASSERT
		mockMvc.perform(get("/patient/999"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
	}

	@Test
	void UpdatePatient_shouldSucceed() throws Exception {
		//ARRANGE
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		MvcResult result = mockMvc.perform(
				put("/patient/1")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
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
	void PutPatient_IsNotFoundExpected() throws Exception {
		//ARRANGE
		String jsonContent = objectMapper.writeValueAsString(patient1);

		//ACT+ASSERT
		mockMvc.perform(put("/patient/999")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));
	}

	@Test
	void CreatePatient_shouldSucceed() throws Exception {
		//ARRANGE
		patient2.setId(1);
		String jsonContent = objectMapper.writeValueAsString(patient2);

		//ACT+ASSERT
		MvcResult result = mockMvc.perform(
				post("/patient/add")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
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
	void CreatePatient_AlreadyExistExpected() throws Exception {
		patient1.setId(1);
		String jsonContent = objectMapper.writeValueAsString(patient1);
		
		//ACT+ASSERT
		mockMvc.perform(post("/patient/add")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isConflict())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientAlreadyExistException));
	}
	
	@Test
	void DeletePatient_shouldSucceed() throws Exception {
		//ACT+ASSERT
		mockMvc.perform(delete("/patient/delete/1"));

		//check patient delete:
		assertFalse(patientRepository.existsById(1));
		assertEquals(3,patientRepository.findAll().size());
	}

	@Test
	void DeletePatient_DoesntExistExpected() throws Exception {
		//ACT+ASSERT
		mockMvc.perform(delete("/patient/delete/9999999"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof PatientNotFoundException));

		//check no patient delete:
		assertEquals(4,patientRepository.findAll().size());
	}
	
	@Test
	void existPatientByFirstNameLastname() throws Exception {
		//ARRANGE
		patient2.setId(1);
		String jsonContent = objectMapper.writeValueAsString(patient2);
		
		//ACT+ASSERT
		MvcResult result = 
				mockMvc.perform(
				post("/patient/exist")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
				.andExpect(status().isOk())
				.andReturn();

		//check patient update:
		Boolean booleanResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Boolean>() {});
		assertEquals(Boolean.FALSE,booleanResult);
	}

}
