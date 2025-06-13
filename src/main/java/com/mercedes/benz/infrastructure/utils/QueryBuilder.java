package com.mercedes.benz.infrastructure.utils;

import com.mercedes.benz.infrastructure.adapter.in.model.FilterType;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.function.Predicate;


public class QueryBuilder {

    public static <T> Predicate<T> createPredicate(String field, String searchValue, FilterType filterType, boolean caseSensitive) {
        return t -> {
            try {
                Field f = t.getClass().getDeclaredField(field);
                f.setAccessible(true);
                Object value = f.get(t);
                if (value == null) return false;

                String fieldValue = value.toString();
                String target = searchValue;

                if (!caseSensitive) {
                    fieldValue = fieldValue.toLowerCase();
                    target = target.toLowerCase();
                }

                return switch (filterType) {
                    case CONTAINS -> fieldValue.contains(target);
                    case STARTS_WITH -> fieldValue.startsWith(target);
                    case ENDS_WITH -> fieldValue.endsWith(target);
                    case EQUALS -> fieldValue.equals(target);
                    default -> false;
                };
            } catch (Exception e) {
                throw new RuntimeException("Invalid filter on field: " + field, e);
            }
        };
    }



    public static <T> Comparator<T> createComparator(String field, boolean descending) {
        Comparator<T> comparator = Comparator.comparing(t -> {
            try {
                Field f = t.getClass().getDeclaredField(field);
                f.setAccessible(true);
                return (Comparable<Object>) f.get(t); // Explicit cast here
            } catch (Exception e) {
                throw new RuntimeException("Failed to create comparator for field: " + field, e);
            }
        });

        return descending ? comparator.reversed() : comparator;
    }

}
