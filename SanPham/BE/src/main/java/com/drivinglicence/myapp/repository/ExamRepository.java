package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.Exam;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query(value = "SELECT * FROM exam WHERE type_id=:id", nativeQuery = true)
    List<Exam> findExamByTypeId(@Param("id")Long id);
}
