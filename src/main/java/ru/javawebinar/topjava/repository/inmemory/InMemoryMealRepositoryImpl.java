package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    public Meal save(Meal meal){
        return save(meal, 0);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal);

        // treat case: meal is new
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: meal is user's meal

        else if(getAll(userId).get(meal.getId()) != null)
            return repository.put(meal.getId(), meal);

        // treat case: meal is not user's meal
        return repository.containsKey(meal.getId()) ? null : repository.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.remove(id, getAll(userId).get(id));
    }

    @Override
    public Meal get(int id, int userId) {
        return getAll(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filter(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        Predicate<Meal> condition1 = (meal -> DateTimeUtil.isBetweenDate(meal.getDate(), startDate, endDate));
        Predicate<Meal> condition2 = (meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
        return filter(userId, condition1.and(condition2));
    }

    private List<Meal> filter(int userId, Predicate<? super Meal> predicate){
        return repository.values()
                .stream()
                .filter(oldMeal-> oldMeal.getUserId() == Math.max(userId,0))
                .filter(predicate)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

}

