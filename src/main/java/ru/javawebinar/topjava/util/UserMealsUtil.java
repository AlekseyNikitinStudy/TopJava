package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

    /*
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    */

        System.out.println("Filtered by cycles:");
        filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);

        System.out.println("Filtered by streams:");
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);

        System.out.println("Filtered by streams-2:");
        filteredByStreamsAdvanced(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
    }

    /**
     * Filters list of UserMeal by left-opened hours interval and calories daily limit.<BR>
     * Returns list of UserMealWithExcess (DTO for UserMeal) for elements that match given params.<BR>
     * Uses cycles for iterations.
     *
     * @param meals list of UserMeal.
     * @param startTime left bound of hours interval.
     * @param endTime right bound of hours interval.
     * @param caloriesPerDay calories daily limit.
     *
     * @return list of UserMealWithExcess.
     */
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        /* Calculates sum of calories per distinct day from incoming list and puts it into map.
        Time complexity #1 is O(N). */
        Map<LocalDate, Integer> caloriesSumPerDays = new HashMap<>();
        for (UserMeal um : meals) {
            int caloriesMeal = um.getCalories();
            LocalDate key = um.getDateTime().toLocalDate();
            // If the same day isn't present, then puts new element into map, otherwise sums calories.
            if (caloriesSumPerDays.computeIfPresent(key, (k, v) -> v + caloriesMeal) == null) {
                caloriesSumPerDays.put(key, caloriesMeal);
            }
        }

        /* Creates resulting list and adds elements into it by filtering during iteration over incoming list.
        Time complexity #2 is O(N). */
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal um : meals) {
            LocalDateTime localDateTime = um.getDateTime();
            // Filters by left-opened hours interval.
            if (TimeUtil.isBetweenHalfOpen(localDateTime.toLocalTime(), startTime, endTime)) {
                Integer caloriesSum = caloriesSumPerDays.getOrDefault(localDateTime.toLocalDate(), 0);
                int caloriesMeal = um.getCalories();
                /* Creates DTO for UserMeal, sets excess by comparing sum of calories per distinct day and calories
                  daily limit. */
                result.add(new UserMealWithExcess(localDateTime, um.getDescription(), caloriesMeal,
                         caloriesPerDay < caloriesSum));
            }
        }
        return result;

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }

    /**
     * Filters list of UserMeal by left-opened hours interval and calories daily limit.<BR>
     * Returns list of UserMealWithExcess (DTO for UserMeal) for elements that match given params.<BR>
     * Uses Stream API for iterations.
     *
     * @param meals list of UserMeal.
     * @param startTime left bound of hours interval.
     * @param endTime right bound of hours interval.
     * @param caloriesPerDay calories daily limit.
     *
     * @return list of UserMealWithExcess.
     */
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        /* Calculates sum of calories per distinct day from incoming list and puts it into map using
        Stream API Collectors.
        Time complexity #1 is O(N). */
        Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        /* Creates resulting list and collects elements into it by filtering during iteration over incoming list.
        Time complexity #2 is O(N). */
        return meals.stream()
                // Filters by left-opened hours interval.
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                .collect(ArrayList::new, (list, um) -> {
                    Integer caloriesSum = caloriesSumPerDay.getOrDefault(um.getDateTime().toLocalDate(), 0);
                    int caloriesMeal = um.getCalories();
                    /* Creates DTO for UserMeal, sets excess by comparing sum of calories per distinct day and calories
                    daily limit. */
                    list.add(new UserMealWithExcess(um.getDateTime(), um.getDescription(), caloriesMeal,
                            caloriesPerDay < caloriesSum));
                }, List::addAll);

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }

    /**
     * Filters list of UserMeal by left-opened hours interval and calories daily limit.<BR>
     * Returns list of UserMealWithExcess (DTO for UserMeal) for elements that match given params.<BR>
     * Uses Stream API for iterations and only one iteration over incoming collection.
     *
     * @param meals list of UserMeal.
     * @param startTime left bound of hours interval.
     * @param endTime right bound of hours interval.
     * @param caloriesPerDay calories daily limit.
     *
     * @return list of UserMealWithExcess.
     */
    public static List<UserMealWithExcess> filteredByStreamsAdvanced(List<UserMeal> meals, LocalTime startTime,
                                                                     LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                /* Groups incoming list into map by distinct day (LocalDate).
                Time complexity #1 is O(N). */
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate()))

                /* Iterates day by day and collects elements into resulting list of UserMealWithExcess.
                Time complexity #2 (#3 + #4) is O(2N) = O(N). */
                .entrySet().stream()
                .collect(ArrayList::new, (resultList, listEntry) -> {
                    /* Calculates sum of calories per distinct day.
                    Time complexity #3 is O(N). */
                    int caloriesSum = listEntry.getValue().stream().mapToInt(UserMeal::getCalories).sum();
                    /* Collects elements by filtering during iteration over list of UserMeal for distinct day.
                    Time complexity #4 is O(N). */
                    listEntry.getValue().stream()
                            // Filters by left-opened hours interval.
                            .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                            /* Creates DTO for UserMeal, sets excess by comparing sum of calories per distinct day and
                            calories daily limit. */
                            .forEach(um -> {
                                resultList.add(new UserMealWithExcess(um.getDateTime(), um.getDescription(),
                                        um.getCalories(), caloriesSum > caloriesPerDay));
                            });
                }, List::addAll);

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }
}
