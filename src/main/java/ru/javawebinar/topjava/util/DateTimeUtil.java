package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiPredicate;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static BiPredicate<LocalDate, LocalDate> rightClosed = (lt, ltBound) -> lt.compareTo(ltBound) <= 0;

    public static BiPredicate<LocalTime, LocalTime> rightOpened = (ld, ldBound) -> ld.compareTo(ldBound) < 0;

    public static <T extends Comparable<? super T>> boolean isBetween(T value, T leftBound, T rightBound,
                                                                      BiPredicate<T, T> rightBoundBiPredicate) {
        return value.compareTo(leftBound) >= 0 && rightBoundBiPredicate.test(value, rightBound);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

