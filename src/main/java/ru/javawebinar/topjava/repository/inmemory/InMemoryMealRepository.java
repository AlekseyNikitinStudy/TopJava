package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository extends InMemoryAbstractRepository<Meal> implements MealRepository {

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public List<Meal> getAllByUserId(int userId) {
        log.info("getAll");
        return getAllByUserIdFilteredByDate(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getAllByUserIdFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date");
        return repository.values().stream()
                .filter(meal -> meal.getUserId() != null && meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .sorted(Meal.COMPARATOR_DATE)
                .collect(Collectors.toList());
    }
}

