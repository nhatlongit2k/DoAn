package com.drivinglicence.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A ResultTest.
 */
@Entity
@Table(name = "result_test")
public class ResultTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "score")
    private Float score;

    @Column(name = "number_correct_question")
    private Long numberCorrectQuestion;

    @Column(name = "is_pass")
    private Boolean isPass;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "exam_id")
    private Long examId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResultTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScore() {
        return this.score;
    }

    public ResultTest score(Float score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getNumberCorrectQuestion() {
        return this.numberCorrectQuestion;
    }

    public ResultTest numberCorrectQuestion(Long numberCorrectQuestion) {
        this.setNumberCorrectQuestion(numberCorrectQuestion);
        return this;
    }

    public void setNumberCorrectQuestion(Long numberCorrectQuestion) {
        this.numberCorrectQuestion = numberCorrectQuestion;
    }

    public Boolean getIsPass() {
        return this.isPass;
    }

    public ResultTest isPass(Boolean isPass) {
        this.setIsPass(isPass);
        return this;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public Long getUserId() {
        return this.userId;
    }

    public ResultTest userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExamId() {
        return this.examId;
    }

    public ResultTest examId(Long examId) {
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
        if (!(o instanceof ResultTest)) {
            return false;
        }
        return id != null && id.equals(((ResultTest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultTest{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", numberCorrectQuestion=" + getNumberCorrectQuestion() +
            ", isPass='" + getIsPass() + "'" +
            ", userId=" + getUserId() +
            ", examId=" + getExamId() +
            "}";
    }
}
