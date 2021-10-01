package com.mediscreen.mdiabeteassess.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mediscreen.common.dto.NoteBean;

@FeignClient(name = "microservice-notes", url = "${feign.mnote.url}")
public interface MicroserviceNotesProxyFeign{

    /**
	 * Get list of notes with a specified patient id
	 * @param patId patient id
	 * @return List of notes
	 */
    @GetMapping( value = "/patients/{patId}/notes")
	public List<NoteBean> getListOfNotesByPatientId(@PathVariable Integer patId);
    
	
}
