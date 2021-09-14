package com.mediscreen.clientui.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mediscreen.clientui.beans.PatientBean;

@Controller
public class ClientUIController {
	
	
	
	@GetMapping("/")
    public String accueil(Model model){

        List<PatientBean> patients =  PatientProxy.listeDesProduits();

        model.addAttribute("patients", patients);

        return "Accueil";
    }
	

}
