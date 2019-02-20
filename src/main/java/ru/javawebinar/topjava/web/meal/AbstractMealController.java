package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MealService service;

    public List<MealTo> getAll(int userId, int calories) {
        log.info("getAll");
            return MealsUtil.getWithExcess(service.getAll(userId), calories);
    }

    public MealTo get(int mealId, int userId, int calories) throws NotFoundException{
        log.info("get {}", mealId);
        Optional result = MealsUtil.getWithExcess(
                service.getAll(userId), calories)
                .stream()
                .filter(mealTo -> mealTo.getId() == mealId)
                .findFirst();
        if(result.isPresent())
            return (MealTo) result.get();
        throw new NotFoundException("Meal not found");
    }

    public MealTo create(Meal meal, int userId, int calories){
        log.info("create {}", meal);
        checkNew(meal);
        service.create(meal, userId);
        return get(meal.getId(), userId, calories);
    }

    public void delete(int mealId, int userId) throws NotFoundException {
        log.info("delete {}", mealId);
        service.delete(mealId, userId);

        }

    public MealTo update(Meal meal, int mealId, int userId, int calories){
        log.info("update {} with id={}", meal, mealId);
        assureIdConsistent(meal, mealId);
        service.update(meal, userId);
        return get(meal.getId(), userId, calories);

        }

    public List<MealTo> getFilteredByPeriod(LocalTime startTime, LocalTime endTime,
                                             LocalDate startDate, LocalDate endDate, int userId, int calories)
    {

        log.info("filter {} with id={}", userId);
        LocalTime T1 = (startTime == null ? LocalTime.MIN : startTime);
        LocalTime T2 = (endTime == null ? LocalTime.MAX : endTime);
        LocalDate D1 = (startDate == null ? LocalDate.MIN : startDate);
        LocalDate D2 = (endDate == null ? LocalDate.MAX : endDate);
        Predicate<Meal> dates = meal -> DateTimeUtil.isBetweenDate(meal.getDate(), D1, D2);
        Predicate<Meal> time = meal -> DateTimeUtil.isBetween(meal.getTime(), T1, T2);
        return MealsUtil.getFilteredWithExcess(service.getAll(userId), calories, dates.and(time));
    }

}
