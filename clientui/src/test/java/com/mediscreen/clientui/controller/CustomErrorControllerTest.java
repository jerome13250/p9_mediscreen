package com.mediscreen.clientui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

/**
 * We cannot perform a unit test if we expect to trigger the ErrorController by calling a method from another Controller. That would be an integration test.
 * To unit test the ErrorController class, we just create an instance of the ErrorController class in a unit test and call the methods.
 * https://stackoverflow.com/questions/53889541/is-it-possible-to-use-an-errorcontroller-with-a-webmvctest/53889721#53889721
 * 
 * @author jerome
 *
 */

@ExtendWith(MockitoExtension.class)
class CustomErrorControllerTest {

	private CustomErrorController customErrorController;

	@Mock
	private HttpServletRequest mockHttpServletRequest;

	@Mock
	private Model mockModel;

	@BeforeEach
	void beforeTest()
	{
		customErrorController = new CustomErrorController();
	}

	@ParameterizedTest(name = "Test Error page for errorCode {0}")
	@ValueSource(ints = { 404, 403, 500, 999 })
	void handleError(int errorCode)
	{
		//ARRANGE:
		// mock the desired mockHttpServletRequest functionality.
		doReturn(errorCode).when(mockHttpServletRequest).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		//ACT:
		String viewResult = customErrorController.handleError(mockHttpServletRequest, mockModel);
		
		// Do asserts on the actualResult here.
		assertEquals("error",viewResult);
		switch (errorCode){
		   
	       case 404: 
	    	   verify(mockModel).addAttribute("errorMsg", "Error 404, Page not found!");
	           break;
	       case 403:
	    	   verify(mockModel).addAttribute("errorMsg", "Error 403, Access denied!");
	           break;
	       case 500:
	    	   verify(mockModel).addAttribute("errorMsg","Sorry, error 500 happened. Please contact our support!");
	           break;
	       default:
	    	   verify(mockModel).addAttribute("errorMsg","Sorry an error has happened.");
	           break;
	   }
	}
}


