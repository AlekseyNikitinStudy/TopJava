package ru.javawebinar.topjava.repository;

import java.util.List;

public interface Repository<T> {
    // null if not found, when updated
    T save(T entity);

    // false if not found
    boolean delete(int id);

    // null if not found
    T get(int id);

    List<T> getAll();
}
