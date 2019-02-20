package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController extends AbstractMealController{

    public List<MealTo> getAll() {
        return super.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public MealTo get(int mealId) throws NotFoundException{

        return super.get(mealId, authUserId(), authUserCaloriesPerDay());
    }

    public MealTo create(Meal meal){
        return super.create(meal, authUserId(), authUserCaloriesPerDay());
    }

    public void delete(int mealId) throws NotFoundException{
        super.delete(mealId, authUserId());
    }

    public MealTo update(Meal meal, int mealId) {
        return super.update(meal, mealId, authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredByPeriod(LocalDate startDate,
                                            LocalDate endDate,
                                            LocalTime startTime,
                                            LocalTime endTime) {
        return super.getFilteredByPeriod(
                startTime,
                endTime,
                startDate,
                endDate,
                authUserId(),
                authUserCaloriesPerDay());
    }
}