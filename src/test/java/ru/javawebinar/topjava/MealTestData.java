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
    public static final int MEAL_ID_ADMIN_1 = START_SEQ + 5;
    public static final int MEAL_ID_ADMIN_2 = START_SEQ + 6;
    public static final int MEAL_ID_NOT_FOUND = 10;

    public static final Meal meal_user_1 = new Meal(MEAL_ID_USER_1,
            LocalDateTime.of(2021, 6, 21, 10, 0),
            "breakfast", 400);
    public static final Meal meal_user_2 = new Meal(MEAL_ID_USER_2,
            LocalDateTime.of(2021, 6, 21, 14, 0),
            "dinner", 300);
    public static final Meal meal_user_3 = new Meal(MEAL_ID_USER_3,
            LocalDateTime.of(2021, 6, 22, 8, 0),
            "breakfast-2", 450);
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
        assertThat(actual).usingRecursiveComparison().ignoringFields("user_id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }
}
