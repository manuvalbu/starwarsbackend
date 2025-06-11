package com.mercedes.benz.domain.vo;

import java.util.Comparator;
import java.util.function.Predicate;

public record Query<T>(
        Predicate<T> filter,
        Comparator<T> sort,
        PageRequest page
) {}
