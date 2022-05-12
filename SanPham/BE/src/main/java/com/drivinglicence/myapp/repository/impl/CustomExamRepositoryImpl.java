package com.drivinglicence.myapp.repository.impl;

import com.drivinglicence.myapp.domain.User;
import com.drivinglicence.myapp.repository.CustomExamRepository;
import com.drivinglicence.myapp.service.dto.ExamDTO;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Repository
public class CustomExamRepositoryImpl implements CustomExamRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ExamDTO> getListExamForUser(User userDetails) {
        StringBuilder sql = new StringBuilder("Select e.id id , e.name name," +
            " e.number_of_question numberOfQuestion, e.max_score maxScore, e.time time, e.type_id typeId, " +
            " e.create_by createBy, e.create_time createTime, e.update_by updateBy, e.update_time updateTime, rt.user_id userId, rt.is_pass isPass, " +
            "(case when (rt.user_id is not null)  then 1 else 0 end) as status " +
            "from exam e left join result_test rt on e.id = rt.exam_id");
        if(Objects.nonNull(userDetails)){
            sql.append(" and rt.user_id = :userId");
        }
        NativeQuery query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("name", new StringType())
            .addScalar("numberOfQuestion", new LongType())
            .addScalar("maxScore", new FloatType())
            .addScalar("time", new LongType())
            .addScalar("typeId", new LongType())
            .addScalar("status", new IntegerType())
            .addScalar("createBy", new StringType())
            .addScalar("createTime", new ZonedDateTimeType())
            .addScalar("updateBy", new StringType())
            .addScalar("updateTime", new ZonedDateTimeType())
            .addScalar("userId", new LongType())
            .addScalar("isPass", new BooleanType())
            .setResultTransformer(Transformers.aliasToBean(ExamDTO.class));

        if(Objects.nonNull(userDetails)){
            query.setParameter("userId", userDetails.getId());
        }

        List<ExamDTO> result = query.getResultList();
        return result;
    }

    @Override
    public List<ExamDTO> getListExamForUserWithTypeId(User userDetails, Long id) {
        StringBuilder sql = new StringBuilder("Select e.id id , e.name name," +
            " e.number_of_question numberOfQuestion, e.max_score maxScore, e.time time, e.type_id typeId, " +
            " e.create_by createBy, e.create_time createTime, e.update_by updateBy, e.update_time updateTime, rt.user_id userId, rt.is_pass isPass, rt.id idResultTest," +
            "(case when (rt.user_id is not null)  then 1 else 0 end) as status " +
            "from exam e left join result_test rt on e.id = rt.exam_id");
        if(Objects.nonNull(userDetails)){
            sql.append(" and rt.user_id = :userId");
            sql.append(" WHERE e.type_id = :typeId");
        }
        NativeQuery query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        query
            .addScalar("id", new LongType())
            .addScalar("name", new StringType())
            .addScalar("numberOfQuestion", new LongType())
            .addScalar("maxScore", new FloatType())
            .addScalar("time", new LongType())
            .addScalar("typeId", new LongType())
            .addScalar("status", new IntegerType())
            .addScalar("createBy", new StringType())
            .addScalar("createTime", new ZonedDateTimeType())
            .addScalar("updateBy", new StringType())
            .addScalar("updateTime", new ZonedDateTimeType())
            .addScalar("userId", new LongType())
            .addScalar("isPass", new BooleanType())
            .addScalar("idResultTest", new LongType())
            .setResultTransformer(Transformers.aliasToBean(ExamDTO.class));

        if(Objects.nonNull(userDetails)){
            query.setParameter("userId", userDetails.getId());
            query.setParameter("typeId", id);
        }

        List<ExamDTO> result = query.getResultList();
        return result;
    }
}
