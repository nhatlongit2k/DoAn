package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.ResultQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ResultQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultQuestionRepository extends JpaRepository<ResultQuestion, Long> {
    @Query(value = "SELECT * FROM result_question WHERE question_id=:id", nativeQuery = true)
    List<ResultQuestion> findResultQuestionByQuestionId(@Param("id")Long id);

    List<ResultQuestion> findAllByResulttestId(@Param("id")Long id);

    void deleteAllByResulttestId(@Param("id")Long id);
}
