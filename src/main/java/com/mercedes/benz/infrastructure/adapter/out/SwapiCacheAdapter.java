package com.mercedes.benz.infrastructure.adapter.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.config.SwapiProperties;
import com.mercedes.benz.infrastructure.utils.SwapiApiUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class SwapiCacheAdapter implements PeopleDataSource, PlanetsDataSource {
    private final RestTemplate restTemplate;
    private final SwapiProperties swapiProperties;

    public SwapiCacheAdapter(RestTemplate restTemplate, SwapiProperties swapiProperties) {
        this.restTemplate = restTemplate;
        this.swapiProperties = swapiProperties;
    }

    private List<Person> people = Collections.emptyList();
    private List<Planet> planets = Collections.emptyList();

    public List<Person> getPeople() {
        return people;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    @PostConstruct
    @Scheduled(cron = "${cache.cron}")
    public void loadDataFromSwapi() {
        String peopleUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().people();
        String planetsUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().planets();
        people = SwapiApiUtil.fetchPeople(restTemplate, peopleUrl);
        planets = SwapiApiUtil.fetchPlanets(restTemplate, planetsUrl);
    }

    @Override
    public List<Person> getPeopleFromSource(Query<Person> query) {
        return people.stream()
                .filter(query.filter())
                .sorted(query.sort() != null ? query.sort() : Comparator.comparing(Person::name))
                .skip(query.page().offset())
                .limit(query.page().limit())
                .toList();
    }

    @Override
    public List<Planet> getPlanetsFromSource(Query<Planet> query) {
        return planets.stream()
                .filter(query.filter())
                .sorted(query.sort() != null ? query.sort() : Comparator.comparing(Planet::name))
                .skip(query.page().offset())
                .limit(query.page().limit())
                .toList();
    }
}
