package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("hsqldb")
@Repository
public class HsqldbJdbcMealRepositoryImpl extends AbstractJdbcMealRepository {

    @Autowired
    public HsqldbJdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);

    }

    @Override
    Comparable convertDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }


}
