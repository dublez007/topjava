package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> li = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        for (UserMealWithExceed meal: li)
            System.out.println(meal.toString());
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesMap = new TreeMap<>();
        Iterator<UserMeal> it = mealList.iterator();

        while(it.hasNext()){
            UserMeal m = it.next();
            caloriesMap.merge(m.getDateTime().toLocalDate(), m.getCalories(), (a, b) -> a + b);
        }

        List<UserMealWithExceed> result = new ArrayList<>();

        for(UserMeal meal: mealList) {
            LocalTime t = meal.getDateTime().toLocalTime();
            if (t.compareTo(startTime) >= 0 && t.compareTo(endTime) <= 0) {
                boolean caloriesExceeded = caloriesMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                result.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesExceeded));
            }
        }

        // TODO return filtered list with correctly exceeded field
        return result;

    }
}
