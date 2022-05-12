package com.drivinglicence.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_fall_question")
    private Boolean isFallQuestion;

    @Column(name = "exam_id")
    private Long examId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Question content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return this.image;
    }

    public Question image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReason() {
        return this.reason;
    }

    public Question reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsFallQuestion() {
        return this.isFallQuestion;
    }

    public Question isFallQuestion(Boolean isFallQuestion) {
        this.setIsFallQuestion(isFallQuestion);
        return this;
    }

    public void setIsFallQuestion(Boolean isFallQuestion) {
        this.isFallQuestion = isFallQuestion;
    }

    public Long getExamId() {
        return this.examId;
    }

    public Question examId(Long examId) {
        this.setExamId(examId);
        return this;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", reason='" + getReason() + "'" +
            ", isFallQuestion='" + getIsFallQuestion() + "'" +
            ", examId=" + getExamId() +
            "}";
    }
}
