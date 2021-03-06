package com.mediscreen.mnote.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.common.exception.BadRequestException;
import com.mediscreen.common.exception.NotFoundException;
import com.mediscreen.mnote.model.Note;
import com.mediscreen.mnote.model.NoteCounter;
import com.mediscreen.mnote.repository.NoteRepository;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class NoteController {

	@Autowired
	NoteRepository noteRepository;

	/**
	 * endpoint that returns list of all notes in database
	 * @return all notes
	 */
	@ApiOperation(value = "This endpoint returns all notes.")
	@GetMapping(value = "/notes")
	public List<Note> getAllNotes() {

		return noteRepository.findAll();
	}

	/**
	 * endpoint that returns note with a specified id
	 * @param id the note id
	 * @return note object
	 * @throws NotFoundException if note id does not exist in database
	 */
	@ApiOperation(value = "This endpoint returns a note from its id.")
	@GetMapping( value = "/notes/{id}")
	public Optional<Note> getNote(
			@ApiParam(
					value = "Note id",
					example = "614c63a6348c0b2d51be08dd")
			@PathVariable String id) 
					throws NotFoundException {

		Optional<Note> note = noteRepository.findById(id);
		if(!note.isPresent())  throw new NotFoundException("La note correspondant à l'id " + id + " n'existe pas ");

		return note;
	}

	/**
	 * endpoint that updates note with a specified id
	 * @param updatedNote the updated note
	 * @return updated note result
	 * @throws NotFoundException if note id does not exist in database
	 * @throws BadRequestException  if note id is null
	 */
	@ApiOperation(value = "This endpoint updates a note.")
	@PutMapping( value = "/notes")
	public Note updateNote(
			@ApiParam(
					value = "Note object in json format"
					)
			@Valid @RequestBody Note updatedNote)
					throws NotFoundException, BadRequestException {
		
		//id must be set in updatedPatient
		if(updatedNote.getId()==null) throw new BadRequestException("Note id can not be null!");
		
		Optional<Note> oldNote = noteRepository.findById(updatedNote.getId());
		if(!oldNote.isPresent())  throw new NotFoundException("Modification impossible, la note correspondant à l'id " + updatedNote.getId() + " n'existe pas");
		
		return noteRepository.save(updatedNote);
	}

	/**
	 * endpoint that creates note.
	 * @param newNote the note to create
	 * @return created note with id in database.
	 */
	@ApiOperation(value = "This endpoint creates a note.")
	@PostMapping( value = "/notes/add")
	public Note createNote(
			@ApiParam(
					value = "Note object in json format"
					)
			@Valid @RequestBody Note newNote) 
			{
		
		//we use insert to create a new note. To avoid id collision we set it to null to let database create id.
		newNote.setId(null);
		return noteRepository.insert(newNote);
	} 

	/**
	 * endpoint that deletes note with a specified id
	 * @param id the note id
	 * @throws NotFoundException if note id does not exist in database
	 */
	@ApiOperation(value = "This endpoint deletes a note.")
	@DeleteMapping( value = "/notes/delete/{id}")
	public void deleteNote(
			@ApiParam(
					value = "Note id",
					example = "6152e0fb4768e6090b7856d8")
			@PathVariable String id
			) throws NotFoundException {

		if(!noteRepository.existsById(id)) throw new NotFoundException("Suppression impossible, la note correspondant à l'id " + id + " n'existe pas");
		noteRepository.deleteById(id);
	}
	
	/**
	 * endpoint that returns list of notes with a specified patient id
	 * @param patId patient id
	 * @return List of notes
	 */
	@ApiOperation(value = "This endpoint returns a list of notes for a patient id.")
	@GetMapping( value = "/patients/{patId}/notes")
	public List<Note> getListOfNotesByPatientId(
			@ApiParam(
					value = "Patient id",
					example = "1")
			@PathVariable Integer patId) 
			{

		return noteRepository.findByPatId(patId);
	}

	/**
	 * endpoint that deletes all notes with a specified patient id
	 * @param patId patient id
	 */
	@ApiOperation(value = "This endpoint deletes all notes with the required patient id.")
	@DeleteMapping( value = "/patients/{patId}/notes/delete")
	public void deleteAllNotesByPatientId(
			@ApiParam(
					value = "Patient id",
					example = "1")
			@PathVariable Integer patId
			) {

		noteRepository.deleteAllByPatId(patId);
	}
	
	/**
	 * endpoint that returns the count of notes per patient
	 * @return HashMap with key=patient id, value=count of notes
	 */
	@ApiOperation(value = "This endpoint returns a HashMap with key=patient id and value=count of notes for this patient.")
	@GetMapping( value = "/notes/count")
	public Map<Integer, Integer> getCountOfNotesPerPatient() {
		AggregationResults<NoteCounter> result = noteRepository.getNoteCounters();
		
		List<NoteCounter> listNoteCounters = result.getMappedResults();
		
		//convert List to HashMap:
        Map<Integer, Integer> mapNoteCounters = new HashMap<>();
        for( NoteCounter noteCounter : listNoteCounters ){
        	mapNoteCounters.put( noteCounter.getPatId(), noteCounter.getCount());            
        }
		
		return mapNoteCounters;
	}
	


}









