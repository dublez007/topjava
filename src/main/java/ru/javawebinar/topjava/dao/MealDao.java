package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.dao.mock.DataSource;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public class MealDao {
    private final DataSource dataSource;

    public MealDao(){
        dataSource = new DataSource();
    }

    public List<Meal> getAllMeals(){
        return dataSource.getMeals();
    }

    public Meal createOrUpdate(Meal meal){
        return dataSource.createOrUpdateMeal(meal);
    }

    public Meal create(LocalDateTime dateTime, String description, int calories){
        return dataSource.createMeal(dateTime.minusHours(1), description, calories);
    }

    public Meal getMealById(int id){
        return dataSource.getMealById(id);
    }

    public void delete(int id){
        dataSource.deleteMeal(id);
    }

}
