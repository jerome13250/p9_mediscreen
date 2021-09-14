package com.mediscreen.clientui.beans;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {

	private Integer id;
	String given;
	String family;
	LocalDate dob;
	char sex;
	String address;
	String phone;
	
}