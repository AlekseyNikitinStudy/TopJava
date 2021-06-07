package ru.javawebinar.topjava.dao;

import java.util.List;

/**
 * Interface for CRUD data source.
 *
 * @param <T> type of records.
 */

public interface CrudDatasource<T> {
    /**
     * Add new record into data source.
     *
     * @param record new record.
     */
    T add(T record);

    /**
     * Update record in data source.
     *
     * @param record record which have to update.
     */
    T update(T record);

    /**
     * Delete record from data source.
     *
     * @param id identifier of record which have to delete.
     */
    void delete(long id);

    /**
     * Get record having identifier.
     *
     * @param id identifier for record which have to get.
     * @return record having identifier of null if there isn't matching record.
     */
    T getById(long id);

    /**
     * Get all records from data source.
     *
     * @return list of records.
     */
    List<T> getAll();
}
