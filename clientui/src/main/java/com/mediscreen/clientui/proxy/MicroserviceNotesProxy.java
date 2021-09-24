package com.mediscreen.clientui.proxy;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.mediscreen.clientui.beans.NoteBean;

public interface MicroserviceNotesProxy {

	/**
	 * returns list of all notes in database
	 * @return all notes
	 */
	public List<NoteBean> getAllNotes();
	
	/**
     * returns note with a specified id
     * @param id the note id
     * @return note
     */
    public NoteBean getNote(String id);

    /**
     * updates note with a specified id
     * @param id the note id
     * @return updated note
     */
    public NoteBean updateNote(NoteBean newNote);
    
    /**
     * creates note.
     * @return created note with id in database.
     */
    public NoteBean createNote(NoteBean newNote);

    /**
     * delete note
     * @param id the note id
     */
    public void deleteNote(String id);
    
    /**
	 * Get list of notes with a specified patient id
	 * @param patId patient id
	 * @return List of notes
	 */
    public List<NoteBean> getListOfNotesByPatientId(Integer patId);
    
}
