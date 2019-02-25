package ru.javawebinar.topjava.repository.inmemory;


import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {


    //userId, meal
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    public Meal save(Meal meal){
        return save(meal, SecurityUtil.authUserId());
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal);

        // treat case: meal is new
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
//            meal.setUserId(userId);
        }
        return repository.computeIfAbsent(userId, k -> new HashMap<>()).put(meal.getId(), meal);

    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> map = repository.get(userId);
        return map == null ? null : map.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return new ArrayList<>(repository.getOrDefault(userId, new HashMap<>()).values());
    }

    public List<Meal> filter(int userId, Predicate<Meal> filter) {
        return repository
                .getOrDefault(userId, new HashMap<>())
                .values()
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }



}
