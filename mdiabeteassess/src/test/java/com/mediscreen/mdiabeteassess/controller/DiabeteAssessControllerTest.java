package com.mediscreen.mdiabeteassess.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.mediscreen.mdiabeteassess.service.DiabeteAssessService;


@WebMvcTest(controllers = DiabeteAssessController.class) 
public class DiabeteAssessControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DiabeteAssessService diabeteAssessService;
	
	@Test
	void postAssessById() throws Exception {
		//ARRANGE
		when(diabeteAssessService.diabeteAssessCalculate(1)).thenReturn("test string");

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(post("/assess/id")
						.param("patId", "1")
				)
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		String stringResult = result.getResponse().getContentAsString();
		assertNotNull(stringResult);
		assertEquals("test string", stringResult);
	}
	
	@Test
	void postAssessByFamilyName() throws Exception {
		//ARRANGE
		when(diabeteAssessService.diabeteAssessCalculateByFamilyName("name")).thenReturn("test string");

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(post("/assess/familyName")
						.param("familyname", "name")
				)
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		String stringResult = result.getResponse().getContentAsString();
		assertNotNull(stringResult);
		assertEquals("test string", stringResult);
	}
	
	
}
