package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository extends InMemoryAbstractRepository implements MealRepository {
    private static final Comparator<Meal> COMPARATOR_DATE = Comparator.comparing(Meal::getDate).reversed();

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
        MealsUtil.meals.forEach(System.out::println);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        } else {
            MealsUtil.checkUserOwning(meal, userId);
        }

        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        return repository.remove(id, MealsUtil.checkUserOwning(repository.get(id), userId));
    }

    @Override
    public Meal get(int id, int userId) {
        return MealsUtil.checkUserOwning(repository.get(id), userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date");
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));

    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        log.info("getAll filtered");
        return repository.values().stream()
                .filter(meal -> meal.getUserId() != null && meal.getUserId() == userId)
                .filter(filter)
                .sorted(COMPARATOR_DATE)
                .collect(Collectors.toList());
    }
}

