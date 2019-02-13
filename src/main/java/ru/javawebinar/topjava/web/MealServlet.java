package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private static final long serialVersionUID = 1L;
//    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDao dao;

    public MealServlet(){
        super();
        dao = new MealDao();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("fetch meals data from data store");
        List<MealTo> meals = MealsUtil.getFilteredWithExcess(
                dao.getAllMeals(),
                LocalTime.of(0, 0),
                LocalTime.of(23, 0),
                2000);
        request.setAttribute("mealsTo", meals);
        log.debug("forward to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

}
