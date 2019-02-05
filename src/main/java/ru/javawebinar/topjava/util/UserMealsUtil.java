package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> li = getFilteredWithExceededUsingStreams(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed meal : li)
            System.out.println(meal.toString());
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> result = new ArrayList<>();

        for (UserMeal meal : mealList) {
            LocalTime time = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(time, startTime, endTime)) {
                boolean isCaloriesExceeded = caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                result.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isCaloriesExceeded));
            }
        }

        return result;

    }

    public static List<UserMealWithExceed> getFilteredWithExceededUsingStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesByDate =
                mealList.stream()
                        .collect(groupingBy(
                                meal -> meal.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories)));


        return mealList
                .stream()
                .filter(e -> TimeUtil.isBetween(e.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> new UserMealWithExceed(
                        e.getDateTime(),
                        e.getDescription(),
                        e.getCalories(),
                        caloriesByDate.get(
                                e.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());


    }

}
