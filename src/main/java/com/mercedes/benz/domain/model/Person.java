package com.mercedes.benz.domain.model;

public record Person(
        String name,
        String height,
        String mass,
        String hairColor,
        String skinColor,
        String eyeColor,
        String birthYear,
        String gender,
        String homeworld,
        String created
) {}
