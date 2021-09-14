package com.mediscreen.mpatient.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="firstname")
	String given;
	@Column(name="lastname")
	String family;
	@Column(name="birthdate")
	LocalDate dob;
	char sex;
	String address;
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
	public Patient(String given, String family, LocalDate dob, char sex, String address, String phone) {
		super();
		this.given = given;
		this.family = family;
		this.dob = dob;
		this.sex = sex;
		this.address = address;
		this.phone = phone;
	}



}
