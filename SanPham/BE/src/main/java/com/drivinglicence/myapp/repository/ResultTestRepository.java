package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.ResultTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ResultTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultTestRepository extends JpaRepository<ResultTest, Long> {
    @Query(value = "SELECT * FROM result_test WHERE exam_id=:id", nativeQuery = true)
    List<ResultTest> findResultByExamId(@Param("id")Long id);
}
