package Tingeso.system.respositories;

import Tingeso.system.entities.InscriptionEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface InscriptionRepository extends JpaRepository<InscriptionEntity, Integer> {

    @Query(value = "SELECT cod_asig FROM notas WHERE notas.cod_alumno = :cod_alumno",
            nativeQuery = true)
    ArrayList<Integer> groupOne(@Param("cod_alumno") String cod_alumno);

    @Query(value = "SELECT cod_asig FROM notas WHERE notas.cod_alumno = :cod_alumno " +
            "AND notas.nota < 4.0",
            nativeQuery = true)
    ArrayList<Integer> groupTwo(@Param("cod_alumno") String cod_alumno);

    @Query(value = "SELECT cod_asig FROM notas WHERE notas.cod_alumno = :cod_alumno " +
            "AND notas.nota >= 4.0",
            nativeQuery = true)
    ArrayList<Integer> groupThree(@Param("cod_alumno") String cod_alumno);

    // given a cod_asig return the cod_prerreq
    @Query(value = "SELECT cod_prerreq FROM prerrequisitos " +
            "WHERE prerrequisitos.cod_asig = :cod_asig", nativeQuery = true)
    ArrayList<Integer> prerreq(@Param("cod_asig") Integer cod_asig);


    /* VALUES FROM PLAN_ESTUDIOS */
    @Query(value = "SELECT nom_asig FROM plan_estudios WHERE cod_asig = :cod_asig",
            nativeQuery = true)
    String name(@Param("cod_asig") Integer cod_asig);

    @Query(value = "SELECT estudiantes_maximos FROM plan_estudios WHERE cod_asig = :cod_asig",
            nativeQuery = true)
    Integer max(@Param("cod_asig") Integer cod_asig);

    @Query(value = "SELECT estudiantes_inscritos FROM plan_estudios WHERE cod_asig = :cod_asig",
            nativeQuery = true)
    Integer registered(@Param("cod_asig") Integer cod_asig);

    @Query(value = "SELECT horario FROM plan_estudios WHERE cod_asig = :cod_asig",
            nativeQuery = true)
    ArrayList<String> schedule(@Param("cod_asig") Integer cod_asig);

}
