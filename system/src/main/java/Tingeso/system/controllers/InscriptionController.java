package Tingeso.system.controllers;

import Tingeso.system.entities.InscriptionEntity;
import Tingeso.system.entities.SubjectEntity;
import Tingeso.system.services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/post")
    public ResponseEntity<String> enroll(@RequestBody ArrayList<SubjectEntity> subjects){
        inscriptionService.enroll(subjects);
        return ResponseEntity.ok("All good");
    }

    @GetMapping("/level/{cod_alumno}")
    public Integer maxSubjects(@Param("cod_alumo") String cod_alumno){
        return inscriptionService.maxSubjects(cod_alumno);
    }
}
