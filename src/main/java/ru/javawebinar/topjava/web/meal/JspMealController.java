package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MealService service;

    @GetMapping()
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        List<MealTo> mealList = MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealList);
        return "meals";
    }

    @GetMapping(value = "/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        Meal meal = service.get(id, userId);
        model.addAttribute(meal);
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }


    @GetMapping("/create")
    public String create(Model model) {
        Meal m = new Meal(LocalDateTime.now(), "Обед", MealsUtil.DEFAULT_CALORIES_PER_MEAL);

        int userId = SecurityUtil.authUserId();
//        checkNew(meal);
        log.info("create {} for user {}", m, userId);
        Meal meal = service.create(m, userId);
        model.addAttribute(meal);

        return "mealForm";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(HttpServletRequest request){
        int userId = SecurityUtil.authUserId();

        int id = Integer.parseInt(request.getParameter("id"));
        Meal meal = service.get(id, userId);
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);

        return "redirect:/meals";
    }


    @PostMapping()
    public String getBetween(HttpServletRequest request, Model model) {

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));

        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
        List<MealTo> meals = MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);


        model.addAttribute("meals", meals);
        return "meals";

    }
}
