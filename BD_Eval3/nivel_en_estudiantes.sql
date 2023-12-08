WITH min_notas AS (
  SELECT notas.cod_alumno, plan_estudios.nivel, MIN(notas.nota) as min_nota
  FROM notas
  INNER JOIN plan_estudios ON notas.cod_asig = plan_estudios.cod_asig
  GROUP BY notas.cod_alumno, plan_estudios.nivel
)
UPDATE estudiantes
SET nivel = (
  SELECT MAX(CASE 
                WHEN min_notas.min_nota >= 4.0 THEN min_notas.nivel + 1
                ELSE min_notas.nivel
             END)
  FROM min_notas
  WHERE min_notas.cod_alumno = estudiantes.rut
)