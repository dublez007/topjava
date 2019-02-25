package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.*;


@Controller
public class MealRestController{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MealService service;


    public List<MealTo> getAll() {
        log.info("getAll");
        int userId = SecurityUtil.authUserId();
        int calories = SecurityUtil.authUserCaloriesPerDay();
        List<Meal> meals = service.getAll(userId);
        return MealsUtil.getWithExcess(meals, calories);
    }

    public Meal get(int mealId) throws NotFoundException{
        log.info("get {}", mealId);
        int userId = SecurityUtil.authUserId();
        return service.get(mealId, userId);
    }

    public Meal create(Meal meal){
        log.info("create {}", meal);
        int userId = SecurityUtil.authUserId();
        return service.create(meal, userId);
    }

    public void delete(int mealId) throws NotFoundException {
        log.info("delete {}", mealId);
        int userId = SecurityUtil.authUserId();
        service.delete(mealId, userId);

    }

    public Meal update(Meal meal, int mealId){
        log.info("update {} with id={}", meal, mealId);
        assureIdConsistent(meal, mealId);
        int userId = SecurityUtil.authUserId();
        return service.update(meal, userId);
    }

    public List<MealTo> filter(Predicate<Meal> dateFilter, Predicate<MealTo> timeFilter){
        log.info("getByPeriod");
        int userId = SecurityUtil.authUserId();
        int calories = SecurityUtil.authUserCaloriesPerDay();
        List<Meal> meals = service.filter(userId, dateFilter);
        return MealsUtil.getWithExcess(meals, calories)
                .stream()
                .filter(timeFilter)
                .collect(Collectors.toList());
    }

}