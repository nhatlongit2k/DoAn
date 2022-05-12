package com.drivinglicence.myapp.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.drivinglicence.myapp.domain.Question} entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    private String content;

    private String image;

    private String reason;

    private Boolean isFallQuestion;

    private Long examId;

    //1
    private List<AnswerDTO> answerDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsFallQuestion() {
        return isFallQuestion;
    }

    public void setIsFallQuestion(Boolean isFallQuestion) {
        this.isFallQuestion = isFallQuestion;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Boolean getFallQuestion() {
        return isFallQuestion;
    }

    public void setFallQuestion(Boolean fallQuestion) {
        isFallQuestion = fallQuestion;
    }

    public List<AnswerDTO> getAnswerDTOList() {
        return answerDTOList;
    }

    public void setAnswerDTOList(List<AnswerDTO> answerDTOList) {
        this.answerDTOList = answerDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", reason='" + getReason() + "'" +
            ", isFallQuestion='" + getIsFallQuestion() + "'" +
            ", examId=" + getExamId() +
            "}";
    }
}
