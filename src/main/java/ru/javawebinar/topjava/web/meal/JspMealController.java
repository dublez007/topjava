package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {


    @Autowired
    private MealRestController mealController;

    @Autowired
    private MealService mealService;

    @GetMapping()
    public String meals(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @GetMapping(value = "/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = mealService.get(id, userId);
        model.addAttribute(meal);
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        int userId = SecurityUtil.authUserId();
        mealService.delete(id, userId);
        return "redirect:/meals";
    }


    @GetMapping("/create")
    public String create(Model model) {
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
            model.addAttribute(meal);
            return "mealForm";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(
            @RequestParam(value="id") String id,
            @RequestParam(value="dateTime") String dateTime,
            @RequestParam(value="description") String description,
            @RequestParam(value="calories") String calories){

        int userId = SecurityUtil.authUserId();
        Meal meal = mealService.getWithUser(Integer.parseInt(id), userId);
        meal.setDescription(description);
        meal.setDateTime(LocalDateTime.parse(dateTime));
        meal.setCalories(Integer.parseInt(calories));
        mealService.update(meal, userId);
        return "redirect:/meals";
    }


    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String getBetween(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";

    }
}
