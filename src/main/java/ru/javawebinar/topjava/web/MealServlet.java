package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private static final long serialVersionUID = 1L;
//    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private static String MEAL = "/meal.jsp";
    private MealDao dao;

    public MealServlet(){
        super();
        dao = new MealDao();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forwardPage = "";
        String action=request.getParameter("action");
        if(action == null)
            throw new NullPointerException("'action' parameter is null");

        else if(action.equalsIgnoreCase("listMeals")) {
            forwardPage = LIST_MEAL;

            log.debug("fetch meals data from data store");
            List<MealTo> meals = getMeals();
            request.setAttribute("mealsTo", meals);
            log.debug("forward to meals page");
        }

        else if(action.equalsIgnoreCase("insert")){
            forwardPage = MEAL;
            log.debug("forward to meal page");
        }


        else if(action.equalsIgnoreCase("update")){
            forwardPage = MEAL;
            log.debug("pulling data to meal page");
            String id = request.getParameter("mealId");
            if(id == null)
                throw new NullPointerException("mealId is null");

            int mealId = Integer.parseInt(id);
            if( mealId == 0)
                throw new NullPointerException("mealId = 0");

            else {
                for( MealTo mealTo : getMeals()){
                    if(mealTo.getId() == mealId)
                        request.setAttribute("mealTo", mealTo);

                }
            }


        }

        else if(action.equalsIgnoreCase("delete")){
            forwardPage = LIST_MEAL;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(mealId);
            log.debug("fetch meals data from data store");
            List<MealTo> meals = getMeals();
            request.setAttribute("mealsTo", meals);
            log.debug("forward to meals page");
        }
            RequestDispatcher view = request.getRequestDispatcher(forwardPage);
            view.forward(request, response);

    }

    private List<MealTo> getMeals() {
        return MealsUtil.getFilteredWithExcess(
                dao.getAllMeals(),
                LocalTime.of(0, 0),
                LocalTime.of(23, 0),
                2000);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("mealId");
        String dateTime = request.getParameter("dateTime");
        String description = request.getParameter("mealDescription");
        int calories = Integer.parseInt(request.getParameter("mealCalories"));

        LocalDateTime localDateTime = LocalDateTime.now();
        try{
            localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e){
            log.debug("Date time format is incorrect: " + dateTime + " " + "symbols: " + dateTime.length());
        }
        if(dateTime.isEmpty())
        {
            log.debug("datetime is empty");
        }

        if(mealId == null || (mealId.isEmpty() || Integer.parseInt(mealId) == 0)){
            dao.create(localDateTime, description, calories);
        }
        else
        {
            dao.createOrUpdate(new Meal(Integer.parseInt(mealId), localDateTime, description, calories));
        }


        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        List<MealTo> meals = getMeals();
        request.setAttribute("mealsTo", meals);
        view.forward(request, response);
    }
}
