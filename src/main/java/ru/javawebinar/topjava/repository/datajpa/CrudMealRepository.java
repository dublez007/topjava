package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);


    @Override
    @Transactional
    Meal save(Meal meal);


    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    Meal find(@Param("id") Integer id, @Param("userId") int userId);


    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDateTime AND :endDateTime ORDER BY m.dateTime DESC")
    List<Meal> findAllByDateTimeBetween(
            @Param("startDateTime") LocalDateTime start,
            @Param("endDateTime") LocalDateTime end,
            @Param("userId") int userId
    );

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user JOIN m.user u WHERE m.id = :id AND u.id = :userId")
    Meal findWithUser(@Param("id") Integer id, @Param("userId") int userId);

}
