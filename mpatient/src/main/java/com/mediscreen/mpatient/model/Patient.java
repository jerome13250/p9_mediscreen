package com.mediscreen.mpatient.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {

	@ApiModelProperty(
			  value = "id of patient",
			  example = "1")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ApiModelProperty(
			  value = "First name of patient",
			  example = "John",
			  required = true)
	@Column(name="firstname")
	@NotBlank
	String given;
	
	@ApiModelProperty(
			  value = "Last name of patient",
			  example = "Doe",
			  required = true)
	@Column(name="lastname")
	@NotBlank
	String family;
	
	@ApiModelProperty(
			  value = "Patient's date of birth",
			  example = "2021-09-17",
			  required = true)
	@Column(name="birthdate")
	@NotNull
	LocalDate dob;
	
	@ApiModelProperty(
			value = "Patient's gender",
			example = "M",
			allowableValues= "F, M",
			required = true)
	@NotBlank
	@Pattern(regexp = "[FM]")
	String sex;
	
	@ApiModelProperty(
			value = "Patient's postal address",
			example = "1st street New-York",
			required = false)
	String address;
	
	@ApiModelProperty(
			value = "Patient's phone number",
			example = "111-222-333",
			required = false)
	String phone;

	/**
	 * Constructor without id setting, this allows to create new patient which id is set by the database.
	 * @param given firstname
	 * @param family lastname
	 * @param dob date of birth
	 * @param sex gender
	 * @param address postal address
	 * @param phone number
	 */
	public Patient(String given, String family, LocalDate dob, String sex, String address, String phone) {
		super();
		this.given = given;
		this.family = family;
		this.dob = dob;
		this.sex = sex;
		this.address = address;
		this.phone = phone;
	}



}
