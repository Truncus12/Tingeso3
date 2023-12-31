package Tingeso.system.services;

import Tingeso.system.entities.SubjectEntity;
import Tingeso.system.respositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class InscriptionService {

    @Autowired
    InscriptionRepository inscriptionRepository;

    // used multiple times
    public String code_student;

    // Get cods of subjects student can enroll
    public ArrayList<Integer> codSubjects(String cod_alumno){
        code_student = cod_alumno;

        // cursed subjects
        ArrayList<Integer> enrolled = inscriptionRepository.groupOne(cod_alumno);

        // finalGroup = all cod_asig
        ArrayList<Integer> finalGroup = new ArrayList<>();
        for (int i = 1; i <= 58; i++) {
            finalGroup.add(700+i);
        }
        finalGroup.removeAll(enrolled);

        // groupTwo = subjects < 4.0
        ArrayList<Integer> notPassed = inscriptionRepository.groupTwo(cod_alumno);

        // groupThree = subjects > 4.0
        ArrayList<Integer> passed = inscriptionRepository.groupThree(cod_alumno);

        // check if pre-requirements are meet
        Iterator<Integer> iterator = finalGroup.iterator();
        while (iterator.hasNext()){
            ArrayList<Integer> prerreq = inscriptionRepository.prerreq(iterator.next());
            if (!(passed.containsAll(prerreq))){
                iterator.remove();
            }
        }

        // add group of unpassed subjects
        finalGroup.addAll(notPassed);
        return finalGroup;
    }

    // transform the finalGroup into a format more accessible for front
    public ArrayList<SubjectEntity> toSubject(ArrayList<Integer> codsSubjects){

        ArrayList<SubjectEntity> subjects = new ArrayList<>();

        // pass all values to format subject
        for (Integer code : codsSubjects) {
            SubjectEntity aux = new SubjectEntity();
            String name = inscriptionRepository.name(code);
            Integer max = inscriptionRepository.max(code);
            Integer registered = inscriptionRepository.registered(code);
            ArrayList<String> schedule = inscriptionRepository.schedule(code);

            aux.setName(name);
            aux.setMax(max);
            aux.setRegistered(registered);
            aux.setSchedule(schedule);

            subjects.add(aux);
        }

        return subjects;
    }

    // so subjects dont reappear
    // as available for enrollment
    public ResponseEntity<String> enroll(ArrayList<SubjectEntity> subjects){

        // check that there are no schedules that collide
        Integer collide = 0;
        // for every subject get the schedule
        for (SubjectEntity subject : subjects){
            collide = 0;
            ArrayList<String> schedule = subject.getSchedule();
            for (String block : schedule){
                for (SubjectEntity subjectEntity : subjects){
                    if (subjectEntity.getSchedule().contains(block)){
                        collide++;
                        // the only constant colission is between the subject and itself
                        // subject = subjectEntity
                    }
                }
            }
            if (collide > 1){
                return ResponseEntity.badRequest().body("Schedule collision");
            }
        }

        ArrayList<Integer> codes_subjects = new ArrayList<>();

        for (SubjectEntity subject : subjects){
            codes_subjects.add(inscriptionRepository.byName(subject.getName()));
        }

        ArrayList<Float> aux;

        for (Integer code : codes_subjects){
            aux = inscriptionRepository.grades(code, code_student);

            // if there is no 'nota' already with the subject im enrolling
            if (aux.isEmpty()){
                // im creating a new 'nota'
                inscriptionRepository.setEnrolledNew(code_student,code);
            } else {
                // im changing a 'nota'
                inscriptionRepository.setEnrolled(code_student, code);
            }
            inscriptionRepository.addStudent(code);
        }

        return ResponseEntity.ok("All good");
    }

    // get max amount of subjects available for enrollment for a student
    // considering the student level
    public Integer maxSubjects(String cod_alumno){
        Integer level = inscriptionRepository.level(cod_alumno);

        return switch (level) {
            case 1, 9, 10 -> 5;
            case 11 -> 1;
            default -> 6;
        };
    }

    /* MALLA */
    public ArrayList<String> passedSubjects(String cod_alumno){
        ArrayList<Integer> codPassed = inscriptionRepository.groupThree(cod_alumno);
        ArrayList<String> passed = new ArrayList<>();
        for (Integer cod: codPassed){
            passed.add(inscriptionRepository.nameFromCod(cod));
        }
        return passed;
    }

    public ArrayList<String> enrolledSubjects(String cod_alumno){
        ArrayList<Integer> codPassed = inscriptionRepository.codEnrolled(cod_alumno);
        ArrayList<String> enrolled = new ArrayList<>();
        for (Integer cod: codPassed){
            enrolled.add(inscriptionRepository.nameFromCod(cod));
        }
        return enrolled;
    }

    // schedule given a subject name
    public ArrayList<String> schedule(String nom_asig){
        return inscriptionRepository.schedule(nom_asig);
    }

    // grades given a subject name
    public ArrayList<Float> grades(String nom_asig, String cod_alumno){
        return inscriptionRepository.grades(inscriptionRepository.byName(nom_asig), cod_alumno);
    }
}