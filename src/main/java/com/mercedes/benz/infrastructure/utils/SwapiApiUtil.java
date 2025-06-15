package com.mercedes.benz.infrastructure.utils;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.infrastructure.adapter.out.dto.PeopleResponse;
import com.mercedes.benz.infrastructure.adapter.out.dto.PlanetsResponse;
import com.mercedes.benz.infrastructure.adapter.out.dto.SwapiPeopleResponse;
import com.mercedes.benz.infrastructure.adapter.out.dto.SwapiPlanetsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SwapiApiUtil {
    private static final Logger log = LoggerFactory.getLogger(SwapiApiUtil.class);

    public static List<Person> fetchPeople(RestTemplate restTemplate, String url) {
        List<Person> people = new ArrayList<>();
        //iterating through all the api pages and combining all results in one list
        while (url != null) {
            SwapiPeopleResponse response = restTemplate.getForObject(url, SwapiPeopleResponse.class);
            if (response != null && response.results != null) {
                List<Person> nextPeople = response.results.stream().map(PeopleResponse::mapToPerson).toList();
                log.debug("Retrieved people from: {} : {}", url, nextPeople);
                people.addAll(nextPeople);
                //new url for next page
                url = response.next;
            } else {
                break;
            }
        }
        return people;
    }

    public static List<Planet> fetchPlanets(RestTemplate restTemplate, String url) {
        List<Planet> planets = new ArrayList<>();
        //iterating through all the api pages and combining all results in one list
        while (url != null) {
            SwapiPlanetsResponse response = restTemplate.getForObject(url, SwapiPlanetsResponse.class);
            if (response != null && response.results != null) {
                List<Planet> nextPlanets = response.results.stream().map(PlanetsResponse::mapToPlanet).toList();
                log.debug("Retrieved planets from: {} : {}", url, nextPlanets);
                planets.addAll(nextPlanets);
                //new url for next page
                url = response.next;
            } else {
                break;
            }
        }
        return planets;
    }
}
