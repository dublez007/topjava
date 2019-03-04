package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {


    public static Map<User, Meal> MEALS;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final Meal MEAL = new Meal(MEAL_ID, LocalDateTime.of(2019, Month.FEBRUARY, 11, 9, 0), "Завтрак", 800);
    public static List<Meal> USER_MEALS;
    public static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2019, Month.FEBRUARY, 13, 8, 0);
    private static final LocalDateTime BETWEEN_DATE_TIME = START_DATE_TIME.plusHours(1);
    public static final LocalDateTime END_DATE_TIME = START_DATE_TIME.plusHours(2);
    public static Meal MEAL_BETWEEN;

    static {
        int counter = MEAL_ID;

        MEALS = new ConcurrentHashMap<>();
        MEALS.put(USER, new Meal(counter++, LocalDateTime.of(2019, Month.FEBRUARY, 11, 9, 0), "Завтрак", 800));
        MEALS.put(ADMIN, new Meal(counter++, LocalDateTime.of(2019, Month.FEBRUARY, 12, 14, 0), "Обед", 1200));
        MEALS.put(USER, new Meal(counter++, LocalDateTime.of(2019, Month.FEBRUARY, 11, 15, 0), "Обед", 1400));
        MEALS.put(USER, new Meal(counter++, LocalDateTime.of(2019, Month.FEBRUARY, 12, 8, 0), "Завтрак", 800));
        MEALS.put(ADMIN, new Meal(counter++, LocalDateTime.of(2019, Month.FEBRUARY, 12, 14, 30), "Обед", 800));
        MEAL_BETWEEN =  new Meal(counter++, BETWEEN_DATE_TIME, "Обед", 800);
        MEALS.put(USER, MEAL_BETWEEN);
        MEALS.put(USER, MEAL);
        USER_MEALS = MEALS.entrySet()
                .stream()
                .filter((e) -> e.getKey().getId() == USER_ID)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }



    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }

}
