package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of CrudDatasource as in-memory data source for Meal.
 *
 * @see CrudDatasource
 * @see Meal
 */

public class InMemoryMealDatasource implements CrudDatasource<Meal> {
    /**
     * Concurrent incremented sequence. Using for generate new unique identifier for Meal record.
     */
    private final AtomicLong idSequence = new AtomicLong();

    /**
     * List of Meals.
     */
    private final Map<Long, Meal> meals;

    /**
     * Default constructor.
     */
    public InMemoryMealDatasource() {
        meals = new ConcurrentHashMap<>();
    }

    /**
     * Add new record into data source.
     *
     * @param record new record.
     */
    @Override
    public Meal add(Meal record) {
        long id = idSequence.incrementAndGet();
        Meal meal = new Meal(id, record.getDateTime(), record.getDescription(), record.getCalories());
        meals.put(id, meal);
        return meal;
    }

    /**
     * Update record in data source.
     *
     * @param record record which have to update.
     */
    @Override
    public Meal update(Meal record) {
        long id = record.getId();
        Meal meal = new Meal(id, record.getDateTime(), record.getDescription(), record.getCalories());
        return meals.replace(id, meal) == null ? null : meal;
    }

    /**
     * Delete record from data source.
     *
     * @param id identifier of record which have to delete.
     */
    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    /**
     * Get record having identifier.
     *
     * @param id identifier of record which have to get.
     * @return record having identifier of null if there isn't matching record.
     */
    @Override
    public Meal getById(long id) {
        return meals.get(id);
    }

    /**
     * Get all records from data source.
     *
     * @return list of records.
     */
    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
