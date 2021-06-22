package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_USER_1, USER_ID);
        MealTestData.assertMatch(meal, meal_user_1);
    }

    @Test
    public void getWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER_1, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_USER_1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER_1, USER_ID));
    }

    @Test
    public void deleteWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER_1, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> between = service.getBetweenInclusive(LocalDate.of(2021, 6, 21),
                LocalDate.of(2021, 6, 21), USER_ID);
        assertMatch(between, meal_user_6, meal_user_5, meal_user_4);
    }

    @Test
    public void getBetweenInclusiveNoFilter() {
        List<Meal> between = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(between, meal_user_10, meal_user_9, meal_user_8, meal_user_7, meal_user_6, meal_user_5, meal_user_4,
                meal_user_3, meal_user_2, meal_user_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, meal_user_10, meal_user_9, meal_user_8, meal_user_7, meal_user_6, meal_user_5, meal_user_4,
                meal_user_3, meal_user_2, meal_user_1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID_USER_1, USER_ID), getUpdated());
    }

    @Test
    public void updateWrongUser() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateDublicateUserAndDatetime() {
        assertThrows(DataAccessException.class, () ->
                service.update(new Meal(null, REPEAT_DATETIME,
                        "bla-bla-bla", 100500), USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createDublicateUserAndDatetime() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, LocalDateTime.of(2021, 6, 21, 10, 0),
                        "breakfast", 400), USER_ID));
    }
}