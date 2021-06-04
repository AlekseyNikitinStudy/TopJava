package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of CrudDatasource as in-memory data source for Meal.
 *
 * @see CrudDatasource
 * @see Meal
 */

public class MealsDatasourceInMemory implements CrudDatasource<Meal> {
    /** List of Meals. */
    private final List<Meal> meals;

    /**
     * Default constructor.
     */
    public MealsDatasourceInMemory() {
        meals = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructor with initial values.
     *
     * @param meals initial list of Meals
     */
    public MealsDatasourceInMemory(List<Meal> meals) {
        this.meals = new CopyOnWriteArrayList<>(meals);
    }

    /**
     * Get amount of records.
     *
     * @return amount of records.
     */
    @Override
    public long size() {
        return meals.size();
    }

    /**
     * Add new record into data source.
     *
     * @param record new record.
     */
    @Override
    public void add(Meal record) {
        meals.add(record);
    }

    /**
     * Update record in data source.
     *
     * @param record record which have to update.
     */
    @Override
    public void update(Meal record) {
        Collections.replaceAll(meals, getById(record.getId()), record);
    }

    /**
     * Delete record from data source.
     *
     * @param id identifier of record which have to delete.
     */
    @Override
    public void delete(long id) {
        meals.removeIf(meal -> meal.getId() == id);
    }

    /**
     * Get record having identifier.
     *
     * @param id identifier of record which have to get.
     * @return record having identifier of null if there isn't matching record.
     */
    @Override
    public Meal getById(long id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst().orElse(null);
    }

    /**
     * Get all records from data source.
     *
     * @return list of records.
     */
    @Override
    public List<Meal> getAll() {
        return meals;
    }
}
