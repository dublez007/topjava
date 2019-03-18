package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbtractUserServiceTest;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends AbtractUserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        User actual = super.service.getWithMeals(ADMIN_ID);
        ru.javawebinar.topjava.UserTestData.assertMatch(actual, ADMIN);
        ru.javawebinar.topjava.MealTestData.assertMatch(actual.getMeals(), ADMIN.getMeals());
    }
}
