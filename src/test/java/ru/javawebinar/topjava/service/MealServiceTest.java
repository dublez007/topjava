package ru.javawebinar.topjava.service;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

public class MealServiceTest {
    private static ConfigurableApplicationContext appCtx;
    private static MealService service;

    @BeforeClass
    public static void beforeClass() throws Exception {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        service = appCtx.getBean(MealServiceImpl.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }


    @Before
    public void setUp() throws Exception {
        JdbcMealRepositoryImpl repository = appCtx.getBean(JdbcMealRepositoryImpl.class);
        repository.save(MEAL, USER_ID);
        MEALS.forEach((k, v) -> repository.save(v, k.getId()));
    }

    @Test
    public void get()throws Exception {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound()throws Exception {
        Meal meal = service.get(MEAL_ID, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception{
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEALS);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> list = service.getBetweenDateTimes(START_DATE_TIME, END_DATE_TIME, USER_ID);
        Assert.assertNotNull(service.get(MEAL_BETWEEN.getId(), USER_ID));
    }

    @Test
    public void getBetweenDateTimesNotFound() throws Exception{
        List<Meal> list = service.getBetweenDateTimes(LocalDateTime.MAX.minusHours(1), LocalDateTime.MAX, USER_ID);
        Assert.assertTrue(service.getAll(USER_ID).isEmpty());
    }


    @Test
    public void delete() throws Exception{
        service.delete(MEAL_ID, USER_ID);
        Assert.assertNull(service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        service.delete(MEAL_ID, ADMIN_ID);
        Assert.assertNotNull(service.get(MEAL_ID, USER_ID));
    }


    @Test
    public void update() throws Exception{
        Meal updated = new Meal(MEAL);
        updated.setCalories(updated.getCalories()+ 100);
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription(updated.getDescription().concat(" updated"));
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL.getId(), USER_ID), updated);
    }


    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception{
        Meal updated = new Meal(MEAL);
        updated.setCalories(updated.getCalories()+ 100);
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription(updated.getDescription().concat(" updated"));
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(MEAL.getId(), USER_ID), updated);
    }

    @Test
    public void create() throws Exception{
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Завтрак", 1000);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID).get(MEAL_ID), newMeal);
    }
}