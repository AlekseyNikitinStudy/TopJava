package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {
    /** Unique identifier. */
    private final long id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(long id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    /**
     * Create empty record.
     *
     * @return empty record.
     */
    public static Meal empty() {
        return new Meal(0,  LocalDateTime.now(), "", 0);
    }

    /**
     * Get identifier.
     *
     * @return identifier.
     */
    public long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
