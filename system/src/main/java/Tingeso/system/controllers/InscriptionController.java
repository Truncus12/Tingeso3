package Tingeso.system.controllers;

import Tingeso.system.entities.InscriptionEntity;
import Tingeso.system.entities.SubjectEntity;
import Tingeso.system.services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("inscription")
public class InscriptionController {

    @Autowired
    InscriptionService inscriptionService;

    @GetMapping("/{cod_alumno}")
    public ArrayList<SubjectEntity> subjects(@PathVariable("cod_alumno") String cod_alumno){
        ArrayList<Integer> codSubjects = inscriptionService.codSubjects(cod_alumno);
        return inscriptionService.toSubject(codSubjects);
    }


}
