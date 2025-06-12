package com.mercedes.benz.infrastructure.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

public class SwapiApiUtil {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PeopleResponse {
        public String name;
        public String height;
        public String mass;
        public String hair_color;
        public String skin_color;
        public String eye_color;
        public String birth_year;
        public String gender;
        public String homeworld;
        public String created;

        public Person mapToPerson() {
            return new Person(name, height, mass, hair_color, skin_color, eye_color, birth_year, gender, homeworld, created);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlanetsResponse {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SwapiPeopleResponse {
        public List<PeopleResponse> results;
        public String next;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SwapiPlanetsResponse {
        public List<PlanetsResponse> results;
        public String next;
    }

    public static List<Person> fetchPeople(RestTemplate restTemplate, String url) {
        List<Person> people = new ArrayList<>();

        while (url != null) {
            SwapiPeopleResponse response = restTemplate.getForObject(url, SwapiPeopleResponse.class);
            if (response != null && response.results != null) {
                people.addAll(response.results.stream().map(PeopleResponse::mapToPerson).toList());
                url = response.next;
            } else {
                break;
            }
        }

        return people;
    }

    public static List<Planet> fetchPlanets(RestTemplate restTemplate, String url) {
        List<Planet> planets = new ArrayList<>();

        while (url != null) {
            SwapiPlanetsResponse response = restTemplate.getForObject(url, SwapiPlanetsResponse.class);
            if (response != null && response.results != null) {
                planets.addAll(response.results.stream().map(PlanetsResponse::mapToPlanet).toList());
                url = response.next;
            } else {
                break;
            }
        }

        return planets;
    }

}
