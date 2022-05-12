package com.drivinglicence.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.drivinglicence.myapp.domain.ResultQuestion} entity.
 */
public class ResultQuestionDTO implements Serializable {

    private Long id;

    private Long idAnswer;

    private Boolean isCorrect;

    private Long resulttestId;

    private Long questionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Long getResulttestId() {
        return resulttestId;
    }

    public void setResulttestId(Long resulttestId) {
        this.resulttestId = resulttestId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultQuestionDTO)) {
            return false;
        }

        ResultQuestionDTO resultQuestionDTO = (ResultQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resultQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultQuestionDTO{" +
            "id=" + getId() +
            ", idAnswer=" + getIdAnswer() +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", resulttestId=" + getResulttestId() +
            ", questionId=" + getQuestionId() +
            "}";
    }
}
