package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM question WHERE exam_id=:id", nativeQuery = true)
    List<Question> findQuestionByExamId(@Param("id")Long id);

//    @Query(value = "DELETE FROM question WHERE exam_id=:id", nativeQuery = true)
//    void deleteQuestionByExamId(@Param("id")Long id);

    void deleteAllByExamId(Long id);
}
