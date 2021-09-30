package com.mediscreen.mnote.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.mnote.exception.BadRequestException;
import com.mediscreen.mnote.exception.NoteNotFoundException;
import com.mediscreen.mnote.model.Note;
import com.mediscreen.mnote.model.NoteCounter;
import com.mediscreen.mnote.repository.NoteRepository;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = NoteController.class) 
class NoteControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private NoteRepository noteRepository;

	Note note1;
	Note note2;
	List<Note> listNote;

	@BeforeEach
	void setup() {
		note1 = new Note("mongodb_id1",1, "note1");
		note2 = new Note("mongodb_id2",2, "note2");
		listNote = new ArrayList<>();
		listNote.add(note1);
		listNote.add(note2);

	}

	@Test
	void GetAllNotes_shouldSucceed() throws Exception {
		//ARRANGE
		when(noteRepository.findAll()).thenReturn(listNote);

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/notes"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		List<Note> listNoteResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Note>>() {});
		assertNotNull(listNoteResult);
		assertEquals(2, listNoteResult.size());
	}

	@Test
	void GetNote_shouldSucceed() throws Exception {
		//ARRANGE
		when(noteRepository.findById("mongodb_id1")).thenReturn(Optional.of(note1));

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/notes/mongodb_id1"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Note noteResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Note>() {});
		assertNotNull(noteResult);
		assertEquals(note1, noteResult);
		assertEquals("mongodb_id1", noteResult.getId());
	}

	@Test
	void GetNote_IsNotFoundExpected() throws Exception {
		//ARRANGE
		when(noteRepository.findById("mongodb_id999")).thenReturn(Optional.empty());

		//ACT+ASSERT
		mockMvc.perform(get("/notes/mongodb_id999"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof NoteNotFoundException));
	}

	@Test
	void UpdateNote_shouldSucceed() throws Exception {
		//ARRANGE
		note2.setId("mongodb_id1"); //objective=update note1 with values of note2
		String jsonContent = objectMapper.writeValueAsString(note2);

		when(noteRepository.findById("mongodb_id1")).thenReturn(Optional.of(note1));
		when(noteRepository.save(any(Note.class))).thenReturn(note2);

		//ACT+ASSERT
		MvcResult result = mockMvc.perform(
				put("/notes")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
				.andExpect(status().isOk())
				.andReturn();

		//check note update:
		ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
		verify(noteRepository,times(1)).save(noteArgumentCaptor.capture());
		Note noteCaptured = noteArgumentCaptor.getValue();
		assertEquals(note2, noteCaptured);
		assertEquals("mongodb_id1",noteCaptured.getId());

		Note noteReturned = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Note>() {});
		assertNotNull(noteReturned);
		assertEquals(note2, noteReturned);
		assertEquals("mongodb_id1",noteReturned.getId());
	}

	@Test
	void PutNote_IsBadRequestExpected() throws Exception {
		//ARRANGE
		note2.setId(null);
		String jsonContent = objectMapper.writeValueAsString(note2);

		//ACT+ASSERT
		mockMvc.perform(put("/notes")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isBadRequest())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
	}

	@Test
	void PutNote_IsNotFoundExpected() throws Exception {
		//ARRANGE
		note2.setId("mongodb_id1"); //objective=update note1 with values of note2
		String jsonContent = objectMapper.writeValueAsString(note2);

		when(noteRepository.findById("mongodb_id1")).thenReturn(Optional.empty());

		//ACT+ASSERT
		mockMvc.perform(put("/notes")
				.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
				)
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof NoteNotFoundException));
	}

	@Test
	void CreateNote_shouldSucceed() throws Exception {
		//ARRANGE
		note2.setId("mongodb_id1");
		when(noteRepository.insert(any(Note.class))).thenReturn(note2);
		String jsonContent = objectMapper.writeValueAsString(note2);

		//ACT+ASSERT
		MvcResult result = 
				mockMvc.perform(
						post("/notes/add")
						.contentType(MediaType.APPLICATION_JSON).content(jsonContent)
						)
				.andExpect(status().isOk())
				.andReturn();

		//check note update:
		ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
		verify(noteRepository,times(1)).insert(noteArgumentCaptor.capture());
		Note noteCaptured = noteArgumentCaptor.getValue();

		assertEquals(note2,noteCaptured);
		assertNull(noteCaptured.getId(),"id is set to null by default since we want a creation");

		Note noteReturned = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Note>() {});
		assertNotNull(noteReturned);
		assertEquals(note2,noteReturned);
		assertEquals("mongodb_id1",noteReturned.getId());
	}

	@Test
	void DeleteNote_shouldSucceed() throws Exception {
		//ARRANGE
		when(noteRepository.existsById("mongodb_id1")).thenReturn(Boolean.TRUE);

		//ACT+ASSERT
		mockMvc.perform(delete("/notes/delete/mongodb_id1"));

		//check note update:
		verify(noteRepository,times(1)).deleteById("mongodb_id1");
	}

	@Test
	void DeleteNote_DoesntExistExpected() throws Exception {
		//ARRANGE
		when(noteRepository.existsById("mongodb_id1")).thenReturn(Boolean.FALSE);

		//ACT+ASSERT
		mockMvc.perform(delete("/notes/delete/mongodb_id1"))
		.andExpect(status().isNotFound())
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof NoteNotFoundException));

		//check note update:
		verify(noteRepository,never()).deleteById("mongodb_id1");
	}

	@Test
	void GetListOfNotesByPatientId() throws Exception {
		//ARRANGE
		when(noteRepository.findByPatId(1)).thenReturn(listNote);

		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/patients/1/notes"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		List<Note> listNoteResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Note>>() {});
		assertNotNull(listNoteResult);
		assertEquals(note1, listNoteResult.get(0));
		assertEquals("mongodb_id1", listNoteResult.get(0).getId());
		assertEquals(note2, listNoteResult.get(1));
		assertEquals("mongodb_id2", listNoteResult.get(1).getId());
	}

	@Test
	void DeleteAllNotesByPatientId() throws Exception {

		//ACT+ASSERT
		mockMvc
		.perform(delete("/patients/1/notes/delete"))
		.andExpect(status().is2xxSuccessful());
		
		verify(noteRepository,times(1)).deleteAllByPatId(1);

	}
	
	@Test
	void getCountOfNotesPerPatient() throws Exception {
		//ARRANGE
		NoteCounter counter1 = new NoteCounter(1,5);
		NoteCounter counter2 = new NoteCounter(2,8);
		List<NoteCounter> listNoteCounter = new ArrayList<>();
		listNoteCounter.add(counter1);
		listNoteCounter.add(counter2);
		
		AggregationResults<NoteCounter> aggregationResult = new AggregationResults<NoteCounter>(listNoteCounter,new Document());
		when(noteRepository.getNoteCounters()).thenReturn(aggregationResult);
		
		//ACT+ASSERT
		MvcResult result = mockMvc
				.perform(get("/notes/count"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Map<Integer, Integer> listNoteCounterResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<Integer, Integer>>() {});
		assertNotNull(listNoteCounterResult);
		assertEquals(5, listNoteCounterResult.get(1));
		assertEquals(8, listNoteCounterResult.get(2));
	}
}
