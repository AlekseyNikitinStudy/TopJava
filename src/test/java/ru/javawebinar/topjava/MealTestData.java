package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_USER_1 = START_SEQ + 2;
    public static final int MEAL_ID_USER_2 = START_SEQ + 3;
    public static final int MEAL_ID_USER_3 = START_SEQ + 4;
    public static final int MEAL_ID_USER_4 = START_SEQ + 5;
    public static final int MEAL_ID_USER_5 = START_SEQ + 6;
    public static final int MEAL_ID_USER_6 = START_SEQ + 7;
    public static final int MEAL_ID_USER_7 = START_SEQ + 8;
    public static final int MEAL_ID_USER_8 = START_SEQ + 9;
    public static final int MEAL_ID_USER_9 = START_SEQ + 10;
    public static final int MEAL_ID_USER_10 = START_SEQ + 11;

    public static final int MEAL_ID_ADMIN_1 = START_SEQ + 5;
    public static final int MEAL_ID_ADMIN_2 = START_SEQ + 6;
    public static final int MEAL_ID_NOT_FOUND = 10;

    public static final LocalDateTime REPEAT_DATETIME = LocalDateTime.of(2021, 6, 20, 10, 0);

    public static final Meal meal_user_1 = new Meal(MEAL_ID_USER_1,
            REPEAT_DATETIME,
            "breakfast-400", 400);
    public static final Meal meal_user_2 = new Meal(MEAL_ID_USER_2,
            LocalDateTime.of(2021, 6, 20, 14, 0),
            "dinner-300", 300);
    public static final Meal meal_user_3 = new Meal(MEAL_ID_USER_3,
            LocalDateTime.of(2021, 6, 20, 18, 0),
            "supper-1000", 1000);
    public static final Meal meal_user_4 = new Meal(MEAL_ID_USER_4,
            LocalDateTime.of(2021, 6, 21, 0, 0),
            "nightmare-2000", 2000);
    public static final Meal meal_user_5 = new Meal(MEAL_ID_USER_5,
            LocalDateTime.of(2021, 6, 21, 10, 0),
            "breakfast-200", 2000);
    public static final Meal meal_user_6 = new Meal(MEAL_ID_USER_6,
            LocalDateTime.of(2021, 6, 21, 15, 0),
            "dinner-500", 500);
    public static final Meal meal_user_7 = new Meal(MEAL_ID_USER_7,
            LocalDateTime.of(2021, 6, 22, 0, 0),
            "midnight light eater-200", 2000);
    public static final Meal meal_user_8 = new Meal(MEAL_ID_USER_8,
            LocalDateTime.of(2021, 6, 22, 13, 0),
            "breakfast-400", 400);
    public static final Meal meal_user_9 = new Meal(MEAL_ID_USER_9,
            LocalDateTime.of(2021, 6, 22, 16, 0),
            "dinner-500", 500);
    public static final Meal meal_user_10 = new Meal(MEAL_ID_USER_10,
            LocalDateTime.of(2021, 6, 22, 23, 59, 59),
            "hungry games-100", 100);

    public static final Meal meal_admin_1 = new Meal(MEAL_ID_ADMIN_1,
            LocalDateTime.of(2021, 6, 21, 10, 0),
            "breakfast", 200);
    public static final Meal meal_admin_2 = new Meal(MEAL_ID_ADMIN_2,
            LocalDateTime.of(2021, 6, 21, 15, 0),
            "dinner", 300);

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2021, 1, 1, 0, 0),
                "new", 100);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal_user_1);
        updated.setDateTime(LocalDateTime.of(2021, 12, 31, 23, 59));
        updated.setDescription("updated");
        updated.setCalories(999);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
