package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM Meal m WHERE m.user.id = ?1 ORDER BY m.dateTime DESC")
    List<Meal> getAll(int userId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM Meal m WHERE m.user.id = ?3 AND m.dateTime >= ?1 AND m.dateTime < ?2 ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
