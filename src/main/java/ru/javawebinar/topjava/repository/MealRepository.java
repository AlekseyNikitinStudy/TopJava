package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends Repository<Meal> {
    List<Meal> getAllByUserId(int userId);

    List<Meal> getAllByUserIdFilteredByDate(int userId, LocalDate startDate, LocalDate endDate);
}
