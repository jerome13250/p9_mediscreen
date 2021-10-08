package com.mediscreen.mnote.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
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
@Document(collection = "note")
public class Note {
	
	@ApiModelProperty(
			  value = "id of note in mongoDB",
			  example = "6152e0fb4768e6090b7856d8")
    @Id
    private String id;

	@ApiModelProperty(
			  value = "id of patient in mySQL",
			  example = "1")
    @NotNull
    private Integer patId;

	@ApiModelProperty(
			  value = "Doctor note for a patient",
			  example = "patient has terrible headache. Excessive weight.")
    @NotBlank
    private String note;

}

