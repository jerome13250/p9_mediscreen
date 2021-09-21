package com.mediscreen.clientui.beans;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class PatientBean {

	private Integer id;
	@NotBlank
	String given;
	@NotBlank
	String family;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd") //Map HTML input date to LocalDate of Java Object
	LocalDate dob;
	
	@NotBlank
	@Pattern(regexp = "[FM]", message="Choix invalide.")
	String sex;
	
	String address;
	String phone;
	
}