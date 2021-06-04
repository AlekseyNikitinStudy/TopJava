package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.CrudDatasource;
import ru.javawebinar.topjava.dao.MealsDatasourceInMemory;
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

    /** Daily calories limit. */
    private static final int CALORIES_PER_DAY = 2000;

    /** Left bound for time filter. */
    private static final LocalTime START_TIME = LocalTime.of(0, 0);

    /** Right bound for time filter. */
    private static final LocalTime END_TIME = LocalTime.of(23, 59);

    /** Patter for input datetime formatter .*/
    private static final String DATETIME_INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm";

    /** Datetime formatter for that servlet. */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_INPUT_PATTERN);

    /** In-memory datasource for Meal. */
    private final CrudDatasource<Meal> mealsDatasourceInMemory = new MealsDatasourceInMemory(MealsUtil.getDefaultMeals());

    /** Default encoding. */
    private final String DEFAULT_ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(DEFAULT_ENCODING);

        log.debug("Get from MealServlet");

        String action = request.getParameter("action");

        // Do necessary actions and go away if parameter "action" was defined.
        if (action != null) {
            String id = request.getParameter("id");
            switch (action) {
                case "create":
                    request.setAttribute("meal", Meal.empty());
                    request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                    return;
                case "update":
                    request.setAttribute("meal", mealsDatasourceInMemory.getById(Long.parseLong(id)));
                    request.getRequestDispatcher("meals_action.jsp").forward(request, response);
                    return;
                case "delete":
                    mealsDatasourceInMemory.delete(Long.parseLong(id));
                    response.sendRedirect("meals");
                    return;
            }
        }

        // Apply filter and show table with meals.
        List<MealTo> meals = MealsUtil.filteredByStreams(mealsDatasourceInMemory.getAll(), START_TIME, END_TIME,
                CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(DEFAULT_ENCODING);

        log.debug("Post from MealServlet");

        String id = request.getParameter("id");

        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        System.out.println(request.getParameter("datetime"));

        LocalDateTime parsedDateTime = LocalDateTime.parse(request.getParameter("datetime"), formatter);

        if (id == null || id.length() == 0 || id.equals("0")) {
            Meal meal = new Meal(MealsUtil.getGeneratedId(), parsedDateTime, description, calories);
            mealsDatasourceInMemory.add(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(id), parsedDateTime, description, calories);
            mealsDatasourceInMemory.update(meal);
        }

        response.sendRedirect("meals");
    }
}
