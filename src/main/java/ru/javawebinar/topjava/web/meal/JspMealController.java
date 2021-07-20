package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
        return "/meals";
    }

    @GetMapping("/delete")
    public String getAll(Model model, @RequestParam @Positive @NotNull Integer id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(Model model, @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate,
                         @RequestParam(required = false) String startTime,
                         @RequestParam(required = false) String endTime) {
        model.addAttribute("meals", getBetween(parseLocalDate(startDate), parseLocalTime(startTime),
                parseLocalDate(endDate), parseLocalTime(endTime)));
        return "/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        return createUpdate(model, meal);
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam @Positive @NotNull Integer id) {
        Meal meal = get(id);
        return createUpdate(model, meal);
    }

    private String createUpdate(Model model, Meal meal) {
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @PostMapping
    public String post(HttpServletRequest request, @RequestParam(required = false) Integer id,
                       @RequestParam @NotNull String dateTime,
                       @RequestParam Integer calories, @RequestParam @NotNull String description) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);

        if (id != null) {
            update(meal, id);
        } else {
            create(meal);
        }

        return "redirect:meals";
    }
}
