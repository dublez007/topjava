package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.*;

//import org.springframework.format.annotation.DateTimeFormat;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    protected MealService mealService;

    private static String REST_URL =  MealRestController.REST_URL + '/';


    @Test
    void testGet() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.MEAL1_ID)
                .sessionAttr("user", USER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealTestData.MEAL1));
    }

    @Test
    void testDelete() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.MEAL1_ID)
                .sessionAttr("user", USER))
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }


    @Test
    void testCreateWithLocation() throws Exception{
        Meal expected = new Meal(LocalDateTime.now(), "New", MealsUtil.DEFAULT_CALORIES_PER_DAY);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .sessionAttr("user", USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());
        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testUpdate() throws Exception{
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Updated meal");
        updated.setCalories(MealsUtil.DEFAULT_CALORIES_PER_DAY * 2);
        updated.setDateTime(LocalDateTime.now());

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
            .sessionAttr("user", USER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updated)))
            .andDo(print())
            .andExpect(status().isNoContent());

        Meal returned = mealService.get(MEAL1_ID, USER_ID);
        updated.setId(returned.getId());

        assertMatch(returned, updated);
    }

    @Test
    void testGetBetween() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "search")
            .sessionAttr("user", USER)
            .param("startDate", "2015-05-30")
            .param("startTime", "00:00:00")
            .param("endDate", "2015-05-30")
            .param("endTime", "22:00:00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(contentJsonTo(
                    MealsUtil.getWithExcess(List.of(MEAL3, MEAL2, MEAL1), USER.getCaloriesPerDay())));
    }
}