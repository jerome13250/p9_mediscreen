package com.mediscreen.mnote.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteCounter {

	@NotNull
    private Integer patId;
	
	@NotNull
    private Integer count;
	
}
