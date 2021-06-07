package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CrudDatasource;
import ru.javawebinar.topjava.dao.InMemoryMealDatasource;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    /** Default encoding. */
    private final String DEFAULT_ENCODING = "UTF-8";

    /** Daily calories limit. */
    private static final int CALORIES_PER_DAY = 2000;

    /** In-memory datasource for Meal. */
    private CrudDatasource<Meal> crudDatasource;

    @Override
    public void init() throws ServletException {
        crudDatasource = new InMemoryMealDatasource();
        MealsUtil.defaultMeals.forEach(crudDatasource::add);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(DEFAULT_ENCODING);
        String idString = request.getParameter("id");

        switch (String.valueOf(request.getParameter("action"))) {
            case "create":
                request.setAttribute("meal", MealsUtil.empty());
                request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                break;
            case "update":
                request.setAttribute("meal", crudDatasource.getById(Long.parseLong(idString)));
                request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                break;
            case "delete":
                long id = Long.parseLong(idString);
                Meal meal = crudDatasource.getById(id);
                crudDatasource.delete(id);
                if (log.isDebugEnabled()) {
                    log.debug("Meal was deleted (id = {}, dateTime = '{}', description = '{}', calories = {})",
                            meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
                }
                response.sendRedirect("meals");
                break;
            default:
                List<MealTo> meals = MealsUtil.filteredByStreams(crudDatasource.getAll(), LocalTime.MIN, LocalTime.MAX,
                        CALORIES_PER_DAY);
                if (log.isDebugEnabled()) {
                    log.debug("All meals were got from datasource and were filtered");
                }
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(DEFAULT_ENCODING);
        String idString = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime parsedDateTime = LocalDateTime.parse(request.getParameter("datetime"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (idString == null || idString.length() == 0 || idString.equals("0")) {
            Meal meal = new Meal(0, parsedDateTime, description, calories);
            if (crudDatasource.add(meal) == null) {
                if (log.isDebugEnabled()) {
                    log.debug("New meal was added (dateTime = '{}', description = '{}', calories = {})",
                            meal.getDateTime(), meal.getDescription(), meal.getCalories());
                }
            } else {
                log.error("Failed to add new meal. Internal error.");
            }
        } else {
            Meal meal = new Meal(Long.parseLong(idString), parsedDateTime, description, calories);
            Meal mealOld = crudDatasource.update(meal);

            if (mealOld != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Meal was updated (id = {}, dateTime = '{}' -> '{}', description = '{}' -> '{}', " +
                                    "calories = {} -> {})", meal.getId(), mealOld.getDateTime(), meal.getDateTime(),
                            mealOld.getDescription(), meal.getDescription(), mealOld.getCalories(), meal.getCalories());
                }
            } else {
                log.error("Failed to update meal (id = {}, dateTime = '{}', description = '{}', calories = {})",
                        meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
            }
        }

        response.sendRedirect("meals");
    }
}
