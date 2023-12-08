package Tingeso.system.services;

import Tingeso.system.respositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

}
