package com.drivinglicence.myapp.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.drivinglicence.myapp.domain.ResultTest} entity.
 */
public class ResultTestDTO implements Serializable {

    private Long id;

    private Float score;

    private Long numberCorrectQuestion;

    private Boolean isPass;

    private Long userId;

    private Long examId;

    //1
    private List<ResultQuestionDTO> resultQuestionDTOList;

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public List<ResultQuestionDTO> getResultQuestionDTOList() {
        return resultQuestionDTOList;
    }

    public void setResultQuestionDTOList(List<ResultQuestionDTO> resultQuestionDTOList) {
        this.resultQuestionDTOList = resultQuestionDTOList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getNumberCorrectQuestion() {
        return numberCorrectQuestion;
    }

    public void setNumberCorrectQuestion(Long numberCorrectQuestion) {
        this.numberCorrectQuestion = numberCorrectQuestion;
    }

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultTestDTO)) {
            return false;
        }

        ResultTestDTO resultTestDTO = (ResultTestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resultTestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultTestDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", numberCorrectQuestion=" + getNumberCorrectQuestion() +
            ", isPass='" + getIsPass() + "'" +
            ", userId=" + getUserId() +
            ", examId=" + getExamId() +
            "}";
    }
}
