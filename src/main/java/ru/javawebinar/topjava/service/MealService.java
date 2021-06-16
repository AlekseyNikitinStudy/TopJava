package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal, meal.getUserId());
    }

    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getAllFiltered(int userId, int caloriesPerDay,
                                       LocalDate startDate, LocalTime startTime,
                                       LocalDate endDate, LocalTime endTime) {
        return MealsUtil.getFilteredTos(repository.getAllByDate(userId, startDate, endDate),
                caloriesPerDay, startTime, endTime);
    }
}