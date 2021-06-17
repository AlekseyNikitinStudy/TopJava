package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository extends InMemoryAbstractRepository implements MealRepository {
    private static final Comparator<Meal> COMPARATOR_DATE = Comparator.comparing(Meal::getDate).reversed();

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            int id = meal.getId();
            if (getMeals(userId).get(id) == null) {
                return null;
            }
        }

        Map<Integer, Meal> meals = repository.getOrDefault(userId, new ConcurrentHashMap<>());
        meals.put(meal.getId(), meal);
        repository.put(userId, meals);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> meals = getMeals(userId);
        return meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return getMeals(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllBetweenDates(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date");
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate,
                DateTimeUtil.rightClosed));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        log.info("getAll filtered");
        return repository.getOrDefault(userId, Collections.emptyMap()).values().stream()
                .filter(filter)
                .sorted(COMPARATOR_DATE)
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMeals(int userId) {
        return repository.getOrDefault(userId, Collections.emptyMap());
    }
}
