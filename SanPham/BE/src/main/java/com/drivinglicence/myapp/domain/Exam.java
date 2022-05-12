package com.drivinglicence.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_question")
    private Long numberOfQuestion;

    @Column(name = "max_score")
    private Float maxScore;

    @Column(name = "time")
    private Long time;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfQuestion() {
        return this.numberOfQuestion;
    }

    public Exam numberOfQuestion(Long numberOfQuestion) {
        this.setNumberOfQuestion(numberOfQuestion);
        return this;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public Float getMaxScore() {
        return this.maxScore;
    }

    public Exam maxScore(Float maxScore) {
        this.setMaxScore(maxScore);
        return this;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public Long getTime() {
        return this.time;
    }

    public Exam time(Long time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public Exam typeId(Long typeId) {
        this.setTypeId(typeId);
        return this;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Exam createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Exam updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getCreateTime() {
        return this.createTime;
    }

    public Exam createTime(ZonedDateTime createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return this.updateTime;
    }

    public Exam updateTime(ZonedDateTime updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return id != null && id.equals(((Exam) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numberOfQuestion=" + getNumberOfQuestion() +
            ", maxScore=" + getMaxScore() +
            ", time=" + getTime() +
            ", typeId=" + getTypeId() +
            ", createBy='" + getCreateBy() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
