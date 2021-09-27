package com.mediscreen.mnote.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mediscreen.mnote.model.Note;
import com.mediscreen.mnote.model.NoteCounter;

public interface NoteRepository extends MongoRepository<Note,String>{

	List<Note> findByPatId(Integer patId);
	
	void deleteAllByPatId(Integer patId);
	
	//TODO: Resoudre bug
	//readStartDocument can only be called when CurrentBSONType is DOCUMENT, not when CurrentBSONType is STRING.
	@Query("db.note.aggregate(["
			+ "  { $group : { "
			+ "      _id: \"$patId\","
			+ "      count : { $sum : 1 }"
			+ "     }"
			+ "  },"
			+ "  {"
			+ "    $project: {"
			+ "      _id: 0,"
			+ "      patId: \"$_id\","
			+ "      count: 1"
			+ "    }"
			+ "  }"
			+ "])")
	List<NoteCounter> getNoteCounters();

	
}
