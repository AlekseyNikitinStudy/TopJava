package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
    private static final AtomicLong idSequence = new AtomicLong();

    /**
     * List of Meals.
     */
    private final ConcurrentMap<Long, Meal> meals;

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
        return meals.putIfAbsent(id, new Meal(id, record.getDateTime(), record.getDescription(), record.getCalories()));
    }

    /**
     * Update record in data source.
     *
     * @param record record which have to update.
     */
    @Override
    public Meal update(Meal record) {
        return meals.put(record.getId(), record);
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
        return meals.getOrDefault(id, null);
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
