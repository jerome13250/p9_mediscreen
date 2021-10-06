package com.mediscreen.clientui.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mediscreen.common.dto.NoteBean;

@FeignClient(name = "microservice-notes", url = "${feign.mnote.url}")
public interface NotesProxyFeign{

	/**
	 * returns list of all notes in database
	 * @return all notes
	 */
	@GetMapping(value = "/notes")
	public List<NoteBean> getAllNotes();
	
	/**
     * returns note with a specified id
     * @param id the note id
     * @return note
     */
    @GetMapping( value = "/notes/{id}")
    public NoteBean getNote(@PathVariable String id);

    /**
     * updates note with a specified id
     * @param id the note id
     * @return updated note
     */
    @PutMapping( value = "/notes")
    public NoteBean updateNote(@RequestBody NoteBean updatedNote);
    
    /**
     * creates note.
     * @return created note with id in database.
     */
    @PostMapping( value = "/notes/add")
    public NoteBean createNote(@RequestBody NoteBean newNote);

    /**
     * delete note.
     * @param id the id of the note
     */
    @DeleteMapping( value = "/notes/delete/{id}")
    public void deleteNote(@PathVariable String id);
    
    /**
	 * Get list of notes with a specified patient id
	 * @param patId patient id
	 * @return List of notes
	 */
    @GetMapping( value = "/patients/{patId}/notes")
	public List<NoteBean> getListOfNotesByPatientId(@PathVariable Integer patId);
    
    /**
	 * endpoint that deletes all notes with a specified patient id
	 * @param patId patient id
	 */
	@DeleteMapping( value = "/patients/{patId}/notes/delete")
	public void deleteAllNotesByPatientId(@PathVariable Integer patId);
	
	/**
	 * endpoint that returns the count of notes per patient
	 * @return HashMap with key=patient id, value=count of notes
	 */
	@GetMapping( value = "/notes/count")
	public Map<Integer, Integer> getCountOfNotesPerPatient();
	
}
