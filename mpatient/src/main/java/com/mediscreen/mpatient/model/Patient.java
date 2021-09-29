package com.mediscreen.mpatient.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
@Table(
		name = "patient" , 
		uniqueConstraints = { 
				@UniqueConstraint(name = "UniqueFirstNameAndLastName", columnNames = { "firstname", "lastname" }) 
		}
	)
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
	@Column(name="firstname", nullable = false, length = 100)
	@NotBlank
	@Size(min=1,max=100)
	String given;

	@ApiModelProperty(
			value = "Last name of patient",
			example = "Doe",
			required = true)
	@Column(name="lastname", nullable = false, length = 100)
	@NotBlank
	@Size(min=1,max=100)
	String family;

	@ApiModelProperty(
			value = "Patient's date of birth",
			example = "2021-09-17",
			required = true)
	@Column(name="birthdate", nullable = false)
	@NotNull
	LocalDate dob;

	@ApiModelProperty(
			value = "Patient's gender",
			example = "M",
			allowableValues= "F, M",
			required = true)
	@Column(nullable = false, length = 1)
	@NotBlank
	@Pattern(regexp = "[FM]")
	String sex;

	@ApiModelProperty(
			value = "Patient's postal address",
			example = "1st street New-York",
			required = false)
	@Size(min=0,max=150)
	@Column(length = 150)
	String address;

	@ApiModelProperty(
			value = "Patient's phone number",
			example = "111-222-333",
			required = false)
	@Column(length = 20)
	@Size(min=0,max=20)
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
