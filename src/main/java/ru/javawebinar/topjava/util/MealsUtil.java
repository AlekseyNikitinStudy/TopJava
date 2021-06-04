package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MealsUtil {
    /** Concurrent incremented sequence. Using for generate new unique identifier for Meal record. */
    private static final AtomicLong mealIdSequence = new AtomicLong();

    /** Default values for list of Meals. Using for filling any data for manual testing. */
    private static final List<Meal> DEFAULT_MEALS = Arrays.asList(
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(mealIdSequence.incrementAndGet(),
                    LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static void main(String[] args) {
        List<MealTo> mealsTo = filteredByStreams(DEFAULT_MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    /**
     * Get default values for list of Meals.
     *
     * @return list of Meals
     */
    public static List<Meal> getDefaultMeals() {
        return DEFAULT_MEALS;
    }

    /**
     * Generate next unique identifier for Meal.
     *
     * @return identifier for Meal.
     */
    public static long getGeneratedId() {
        return mealIdSequence.incrementAndGet();
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
