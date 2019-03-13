package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;


@Configuration
@Profile("postgres")
@Repository
public class PostgresJdbcMealRepositoryImpl extends AbstractJdbcMealRepository {

    @Autowired
    public PostgresJdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);

    }

    @Override
    Comparable convertDateTime(LocalDateTime dateTime) {
        return dateTime;
    }

}
