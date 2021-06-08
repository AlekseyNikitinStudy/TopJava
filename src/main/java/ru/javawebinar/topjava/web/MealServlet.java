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
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    /**
     * Default encoding.
     */
    private final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Daily calories limit.
     */
    private static final int CALORIES_PER_DAY = 2000;

    /**
     * In-memory datasource for Meal.
     */
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
                log.debug("Forwarded to meal creation page.");
                request.setAttribute("meal", MealsUtil.empty());
                request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                break;
            case "update":
                long id = Long.parseLong(idString);
                log.debug("Forwarded to meal update page. Meal's id = {}", id);
                request.setAttribute("meal", crudDatasource.getById(id));
                request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                break;
            case "delete":
                id = Long.parseLong(idString);
                crudDatasource.delete(id);
                log.debug("Meal having id = {} was deleted", id);
                response.sendRedirect("meals");
                break;
            default:
                List<MealTo> meals = MealsUtil.filteredByStreams(crudDatasource.getAll(), LocalTime.MIN, LocalTime.MAX,
                        CALORIES_PER_DAY);
                log.debug("All meals were got from datasource and were filtered.");
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
        LocalDateTime parsedDateTime = LocalDateTime.parse(request.getParameter("datetime"));

        if (idString == null || idString.isEmpty()) {
            Meal meal = crudDatasource.add(new Meal(null, parsedDateTime, description, calories));
            if (meal != null) {
                log.debug("New meal was added (id = {}, dateTime = '{}', description = '{}', calories = {}).",
                        meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
            } else {
                log.debug("Failed to add new meal.");
            }
        } else {
            long id = Long.parseLong(idString);
            Meal meal = crudDatasource.update(new Meal(id, parsedDateTime, description, calories));
            if (meal != null) {
                log.debug("Meal was updated (id = {}, dateTime = '{}', description = '{}', calories = {}).",
                        meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
            } else {
                log.debug("Failed to update meal.");
            }
        }

        response.sendRedirect("meals");
    }
}
