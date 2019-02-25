package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.List;
import java.util.function.Predicate;

public interface MealService {

    Meal create(Meal meal, int userId);

    void delete(int mealId, int userId) throws NotFoundException;

    Meal get(int mealId, int userId) throws NotFoundException;

    Meal update(Meal meal, int userId) throws NotFoundException;

    List<Meal> getAll(int userId);

    List<Meal> filter(int userId, Predicate<Meal> filter);

}