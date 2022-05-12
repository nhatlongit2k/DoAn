package com.drivinglicence.myapp.repository;

import com.drivinglicence.myapp.domain.User;
import com.drivinglicence.myapp.service.dto.ExamDTO;

import java.util.List;

public interface CustomExamRepository {
    List<ExamDTO> getListExamForUser(User userDetails);

    List<ExamDTO> getListExamForUserWithTypeId(User userDetails, Long id);
}
