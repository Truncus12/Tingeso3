package Tingeso.system.services;

import Tingeso.system.entities.SubjectEntity;
import Tingeso.system.respositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // add nota null to enrolled subjects so they dont reappear
    // as available for enrollment
    public void enroll(ArrayList<SubjectEntity> subjects){

        ArrayList<Integer> codes_subjects = new ArrayList<>();

        for (SubjectEntity subject : subjects){
            codes_subjects.add(inscriptionRepository.byName(subject.getName()));
        }

        for (Integer code : codes_subjects){
            inscriptionRepository.setEnrolled(code_student, code);
            inscriptionRepository.addStudent(code);
        }
    }
}