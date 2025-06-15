package com.mercedes.benz.infrastructure.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercedes.benz.domain.model.Planet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetsResponse {
    public String name;
    public String rotation_period;
    public String orbital_period;
    public String diameter;
    public String climate;
    public String gravity;
    public String terrain;
    public String surface_water;
    public String population;
    public String created;

    public Planet mapToPlanet() {
        return new Planet(name, rotation_period, orbital_period, diameter, climate, gravity, terrain, surface_water, population, created);
    }
}
