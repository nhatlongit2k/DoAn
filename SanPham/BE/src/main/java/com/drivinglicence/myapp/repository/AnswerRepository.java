package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.Answer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query(value = "SELECT * FROM answer WHERE question_id=:id", nativeQuery = true)
    List<Answer> findAnswerByQuestionId(@Param("id")Long id);

//    @Query(value = "DELETE answer WHERE question_id=:id", nativeQuery = true)
//    List<Answer> deleteAnswerByQuestionId(@Param("id")Long id);

//    @Query(value = "DELETE FROM answer WHERE question_id=:id", nativeQuery = true)
//    void deleteAnswerByQuestionId(@Param("id")Long id);

    void deleteAllByQuestionId(Long id);
}
