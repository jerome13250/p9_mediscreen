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

import com.mediscreen.mnote.exception.NoteNotFoundException;
import com.mediscreen.mnote.model.Note;
import com.mediscreen.mnote.model.NoteCounter;
import com.mediscreen.mnote.repository.NoteRepository;
import com.mediscreen.mnote.exception.BadRequestException;

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
	 * @param id
	 * @return note object
	 * @throws NoteNotFoundException if note id does not exist in database
	 */
	@ApiOperation(value = "This endpoint returns a note from its id.")
	@GetMapping( value = "/notes/{id}")
	public Optional<Note> getNote(
			@ApiParam(
					value = "Note id",
					example = "614c63a6348c0b2d51be08dd")
			@PathVariable String id) 
					throws NoteNotFoundException {

		Optional<Note> note = noteRepository.findById(id);
		if(!note.isPresent())  throw new NoteNotFoundException("La note correspondant à l'id " + id + " n'existe pas");

		return note;
	}

	/**
	 * endpoint that updates note with a specified id
	 * @return updated note
	 * @throws NoteNotFoundException if note id does not exist in database
	 * @throws BadRequestException  if note id is null
	 */
	@ApiOperation(value = "This endpoint updates a note.")
	@PutMapping( value = "/notes")
	public Note updateNote(
			@ApiParam(
					value = "Note object in json format"
					)
			@Valid @RequestBody Note updatedNote)
					throws NoteNotFoundException, BadRequestException {
		
		//id must be set in updatedPatient
		if(updatedNote.getId()==null) throw new BadRequestException("Note id can not be null!");
		
		Optional<Note> oldNote = noteRepository.findById(updatedNote.getId());
		if(!oldNote.isPresent())  throw new NoteNotFoundException("Modification impossible, la note correspondant à l'id " + updatedNote.getId() + " n'existe pas");
		
		return noteRepository.save(updatedNote);
	}

	/**
	 * endpoint that creates note.
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
	 * @param id
	 * @throws NoteNotFoundException if note id does not exist in database
	 */
	@ApiOperation(value = "This endpoint deletes a note.")
	@DeleteMapping( value = "/notes/delete/{id}")
	public void deleteNote(
			@ApiParam(
					value = "Note id",
					example = "1")
			@PathVariable String id
			) throws NoteNotFoundException {

		if(!noteRepository.existsById(id)) throw new NoteNotFoundException("Suppression impossible, le note correspondant à l'id " + id + " n'existe pas");
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
					value = "Parent id",
					example = "1")
			@PathVariable Integer patId) 
			{

		List<Note> listNotes = noteRepository.findByPatId(patId);
		
		return listNotes;
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
        Map<Integer, Integer> mapNoteCounters = new HashMap<Integer, Integer>();
        for( NoteCounter noteCounter : listNoteCounters ){
        	mapNoteCounters.put( noteCounter.getPatId(), noteCounter.getCount());            
        }
		
		return mapNoteCounters;
	}
	


}









