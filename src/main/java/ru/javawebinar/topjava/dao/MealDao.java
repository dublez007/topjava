package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;

public class MealDao {

    public List<Meal> getAllMeals(){
        return MealsUtil.getMeals();
    }
}
