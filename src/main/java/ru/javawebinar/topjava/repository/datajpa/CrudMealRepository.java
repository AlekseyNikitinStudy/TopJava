package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    int deleteByIdAndUserId(int id, int userId);

    List<Meal> findByUserId(int userId, Sort sortDatetime);

    Meal findByIdAndUserId(int id, int userId);

    List<Meal> findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThan(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime, Sort sortDatetime);
}
