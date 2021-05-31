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

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        /* Time complexity #1 is O(N). */
        Map<LocalDate, Integer> caloriesSumPerDays = new HashMap<>();
        for (UserMeal um : meals) {
            int caloriesMeal = um.getCalories();
            LocalDate key = um.getDateTime().toLocalDate();
            if (caloriesSumPerDays.computeIfPresent(key, (k, v) -> v + caloriesMeal) == null) {
                caloriesSumPerDays.put(key, caloriesMeal);
            }
        }

        /* Time complexity #2 is O(N). */
        List<UserMealWithExcess> result = new LinkedList<>();
        for (UserMeal um : meals) {
            LocalDateTime localDateTime = um.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(localDateTime.toLocalTime(), startTime, endTime)) {
                Integer caloriesSum = caloriesSumPerDays.get(localDateTime.toLocalDate());
                int caloriesMeal = um.getCalories();
                result.add(new UserMealWithExcess(localDateTime, um.getDescription(), caloriesMeal,
                        caloriesSum == null ? caloriesPerDay < caloriesMeal : caloriesPerDay < caloriesSum));
            }
        }
        return result;

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        /* Time complexity #1 is O(N). */
        Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        /* Time complexity #2 is O(N). */
        return meals.stream()
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                .collect(LinkedList::new, (list, um) -> {
                    Integer caloriesSum = caloriesSumPerDay.get(um.getDateTime().toLocalDate());
                    int caloriesMeal = um.getCalories();
                    list.add(new UserMealWithExcess(um.getDateTime(), um.getDescription(), caloriesMeal,
                            caloriesSum == null ? caloriesPerDay < caloriesMeal : caloriesPerDay < caloriesSum));
                }, LinkedList::addAll);

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }

    public static List<UserMealWithExcess> filteredByStreamsAdvanced(List<UserMeal> meals, LocalTime startTime,
                                                                     LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                /* Time complexity #1 is O(N). */
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate()))

                /* Time complexity #2 (#3 + #4) is O(2N) = O(N). */
                .entrySet().stream()
                .collect(LinkedList::new, (resultList, listEntry) -> {
                    /* Time complexity #3 is O(N). */
                    int caloriesSum = listEntry.getValue().stream().mapToInt(UserMeal::getCalories).sum();
                    /* Time complexity #4 is O(N). */
                    listEntry.getValue().stream()
                            .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                            .forEach(um -> {
                                resultList.add(new UserMealWithExcess(um.getDateTime(), um.getDescription(),
                                        um.getCalories(), caloriesSum > caloriesPerDay));
                            });
                }, LinkedList::addAll);

        /* Resulting time complexity (#1 + #2) is O(N) + O(N) = O(2N) = O(N). */
    }
}
