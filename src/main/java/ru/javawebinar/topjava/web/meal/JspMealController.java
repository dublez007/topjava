package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController{
    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MealService service;

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String meals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute(super.get(id));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String remove (@PathVariable("id") int id) {
        super.delete(id);
        return "redirect:/meals";
    }


    @GetMapping("/create")
    public String populateNewForm(Model model) {
        model.addAttribute(new Meal(0, LocalDateTime.now(), "Обед", MealsUtil.DEFAULT_CALORIES_PER_MEAL));
        return "mealForm";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Meal meal = (id != 0 ? service.get(id, SecurityUtil.authUserId()) : new Meal());

        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        if(id == 0){
            super.create(meal);
        } else {
            super.update(meal, id);
        }

        return "redirect:/meals";
    }


    @PostMapping
    public String getBetween(HttpServletRequest request, Model model) {

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";

    }

}
