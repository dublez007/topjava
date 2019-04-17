package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {

    private MealsUtil() {
    }

    public static List<MealTo> getTo(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredTo(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTo(Collection<Meal> meals, int caloriesPerDay, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getFilteredTo(meals, caloriesPerDay, meal -> Util.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFilteredTo(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> asTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealTo asTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static Meal createNewFromTo(MealTo newMeal) {
        return new Meal(null, newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories());
    }

    public static Meal updateFromTo(Meal meal, MealTo mealTo) {
        meal.setDateTime(mealTo.getDateTime());
        meal.setDescription(mealTo.getDescription());
        meal.setCalories(mealTo.getCalories());
        return meal;
    }
}