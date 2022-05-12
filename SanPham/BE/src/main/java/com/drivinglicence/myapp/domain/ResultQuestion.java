package com.drivinglicence.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A ResultQuestion.
 */
@Entity
@Table(name = "result_question")
public class ResultQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_answer")
    private Long idAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "resulttest_id")
    private Long resulttestId;

    @Column(name = "question_id")
    private Long questionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResultQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAnswer() {
        return this.idAnswer;
    }

    public ResultQuestion idAnswer(Long idAnswer) {
        this.setIdAnswer(idAnswer);
        return this;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Boolean getIsCorrect() {
        return this.isCorrect;
    }

    public ResultQuestion isCorrect(Boolean isCorrect) {
        this.setIsCorrect(isCorrect);
        return this;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Long getResulttestId() {
        return this.resulttestId;
    }

    public ResultQuestion resulttestId(Long resulttestId) {
        this.setResulttestId(resulttestId);
        return this;
    }

    public void setResulttestId(Long resulttestId) {
        this.resulttestId = resulttestId;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public ResultQuestion questionId(Long questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultQuestion)) {
            return false;
        }
        return id != null && id.equals(((ResultQuestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultQuestion{" +
            "id=" + getId() +
            ", idAnswer=" + getIdAnswer() +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", resulttestId=" + getResulttestId() +
            ", questionId=" + getQuestionId() +
            "}";
    }
}
