package com.mediscreen.clientui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.mediscreen.clientui.proxy.DiabeteAssessProxyFeign;

@WebMvcTest(controllers = DiabeteAssessController.class)
public class DiabeteAssessControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DiabeteAssessProxyFeign diabeteAssessProxy;

	@Test
	void GetAllNotes_shouldSucceed() throws Exception {
		//ARRANGE
		when(diabeteAssessProxy.postAssessById(1)).thenReturn("test string");

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patients/1/diabeteAssess"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		String stringResult = result.getResponse().getContentAsString();
		assertNotNull(stringResult);
		assertEquals("test string", stringResult);
	}
}
