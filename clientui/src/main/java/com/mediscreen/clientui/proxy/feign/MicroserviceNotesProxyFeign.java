package com.mediscreen.clientui.proxy.feign;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mediscreen.clientui.beans.NoteBean;
import com.mediscreen.clientui.proxy.MicroserviceNotesProxy;

//TODO: url is static, @Value not possible, find a way to fix this ?
@FeignClient(name = "microservice-notes", url = "localhost:8082")
public interface MicroserviceNotesProxyFeign extends MicroserviceNotesProxy {

	/**
	 * returns list of all notes in database
	 * @return all notes
	 */
	@Override
	@GetMapping(value = "/note")
	public List<NoteBean> getAllNotes();
	
	/**
     * returns note with a specified id
     * @param id the note id
     * @return note
     */
	@Override
    @GetMapping( value = "/note/{id}")
    public NoteBean getNote(@PathVariable String id);

    /**
     * updates note with a specified id
     * @param id the note id
     * @return updated note
     */
	@Override
    @PutMapping( value = "/note")
    public NoteBean updateNote(@RequestBody NoteBean updatedNote);
    
    /**
     * creates note.
     * @return created note with id in database.
     */
	@Override
    @PostMapping( value = "/note/add")
    public NoteBean createNote(@RequestBody NoteBean newNote);

    /**
     * delete note.
     */
	@Override
    @DeleteMapping( value = "/note/delete/{id}")
    public void deleteNote(@PathVariable String id);
    
    /**
	 * Get list of notes with a specified patient id
	 * @param patId patient id
	 * @return List of notes
	 */
	@Override
    @GetMapping( value = "/note/parent/{patId}")
	public List<NoteBean> getListOfNotesByPatientId(@PathVariable Integer patId);
    
}
