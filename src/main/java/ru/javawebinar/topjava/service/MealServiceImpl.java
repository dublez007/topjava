package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.List;
import java.util.function.Predicate;


import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;


    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }


    @Override
    public Meal create(Meal meal, int userId){
        checkNew(meal);
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int mealId, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    @Override
    public Meal get(int mealId, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(mealId, userId), mealId);
    }

    @Override
    public Meal update(Meal meal, int userId) throws NotFoundException {
        int mealId = meal.getId();
        checkNotFoundWithId(repository.get(mealId, userId), mealId);
        return repository.save(meal, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> filter(int userId, Predicate<Meal> filter){
        return repository.filter(userId, filter);
    }

}