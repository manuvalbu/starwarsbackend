package com.mercedes.benz.domain.model;

public record Planet(
        String name,
        String rotationPeriod,
        String orbitalPeriod,
        String diameter,
        String climate,
        String gravity,
        String terrain,
        String surfaceWater,
        String population,
        String created
) {}
