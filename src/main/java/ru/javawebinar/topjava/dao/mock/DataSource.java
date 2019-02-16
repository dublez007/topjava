package ru.javawebinar.topjava.dao.mock;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

public class DataSource {

    private static final AtomicInteger mealsCounter = new AtomicInteger(0);
    private List<Meal> meals = new ArrayList<>();


    private static DataSource ourInstance = new DataSource();

    public static DataSource getInstance() {
        return ourInstance;
    }

    public DataSource(){
         createMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
         createMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
         createMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
         createMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 1000);
         createMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
         createMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    }


    public List<Meal> getMeals(){
        return meals;
    }

    public Meal createMeal(LocalDateTime dateTime, String description, int calories){
        Meal newMeal = new Meal(mealsCounter.incrementAndGet(), dateTime, description, calories);
        meals.add(newMeal);
        return newMeal;
    }

    private Meal updateMeal(Meal mealToUpdate){
        if(mealToUpdate == null)
            throw new NullPointerException("Meal is null");
        ListIterator<Meal> iterator = meals.listIterator();
        while (iterator.hasNext()){
            int index = iterator.nextIndex();
            Meal meal = iterator.next();
            if(meal.getId() == mealToUpdate.getId()){
                meals.set(index,mealToUpdate);
                return mealToUpdate;
            }
        }

        throw new NullPointerException("Meal is null");
    }

    public void deleteMeal(int mealId){
        meals.removeIf(meal -> meal.getId() == mealId);
    }

    public Meal createOrUpdateMeal(Meal meal) {
        if(meal == null)
            throw new NullPointerException("Meal is null");
        if(meal.getId() == 0)
            return createMeal(
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories());
        else
            return updateMeal(meal);
    }

    public Meal getMealById(int id) {
        for(Meal meal: meals){
            if(meal.getId() == id)
                return meal;
        }
                throw new NullPointerException("Can not find meal id: "+ id);
    }
}
