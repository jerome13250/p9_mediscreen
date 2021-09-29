package com.mediscreen.mnote.repository;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mediscreen.mnote.model.Note;
import com.mediscreen.mnote.model.NoteCounter;

public interface NoteRepository extends MongoRepository<Note,String>{

	List<Note> findByPatId(Integer patId);

	void deleteAllByPatId(Integer patId);

	//https://stackoverflow.com/questions/59697496/how-to-do-a-mongo-aggregation-query-in-spring-data#answer-62865095
	//https://stackoverflow.com/questions/58737718/spring-data-mongodb-aggregation-how-to-project-all-fields-of-grouped-referen
	/**
	 * This is a specific aggregation query that allows to count notes per patient.
	 * Note: This could be easily done by doing a noteRepository.findAll to create ORM on all notes and count them in an
	 * ArrayList<Note> but it would be memory inefficient.
	 * 
	 * @return a AggregationResults of NoteCounter
	 */
	@Aggregation(pipeline = {
			"  { $group : { "
					+ "      _id: \"$patId\","
					+ "      count : { $sum : 1 }"
					+ "     }"
					+ "  }"
					,
					"  { $project: {"
					+ "      _id: 0,"
					+ "      patId: \"$_id\","
					+ "      count: 1"
					+ "    }"
					+ "  }"
	})
	AggregationResults<NoteCounter> getNoteCounters();

	
}
