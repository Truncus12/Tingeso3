package Tingeso.system.respositories;

import Tingeso.system.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {

}
