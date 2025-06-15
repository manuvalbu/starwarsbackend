package com.mercedes.benz.infrastructure.adapter.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.config.SwapiProperties;
import com.mercedes.benz.infrastructure.utils.SwapiApiUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SwapiCacheAdapter implements PeopleDataSource, PlanetsDataSource {
    private static final Logger log = LoggerFactory.getLogger(SwapiCacheAdapter.class);

    private final RestTemplate restTemplate;
    private final SwapiProperties swapiProperties;
    private final AtomicInteger executionCount = new AtomicInteger(0);

    public SwapiCacheAdapter(RestTemplate restTemplate, SwapiProperties swapiProperties) {
        this.restTemplate = restTemplate;
        this.swapiProperties = swapiProperties;
    }

    //in-memory storage, can be replaced by any kind of storage service
    private List<Person> cachedPeople = Collections.emptyList();
    private List<Planet> cachedPlanets = Collections.emptyList();

    public List<Person> retrievePeople() {
        return cachedPeople;
    }
    public List<Planet> retrievePlanets() {
        return cachedPlanets;
    }
    public void storePeople(List<Person> people) {
        this.cachedPeople = people;
    }
    public void storePlanets(List<Planet> planets) {
        this.cachedPlanets = planets;
    }

    public int getExecutionCount() {
        return executionCount.get();
    }

    @PostConstruct
    @Scheduled(cron = "${cache.cron}")
    public void loadDataFromSwapi() {
        log.info("Starting data load from SWAPI...");

        executionCount.incrementAndGet();
        String peopleUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().people();
        String planetsUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().planets();

        List<Person> peopleFetched = SwapiApiUtil.fetchPeople(restTemplate, peopleUrl);
        List<Planet> planetsFetched = SwapiApiUtil.fetchPlanets(restTemplate, planetsUrl);

        this.storePeople(peopleFetched);
        this.storePlanets(planetsFetched);

        log.info("number of people loaded from SWAPI : {}", peopleFetched.size());
        log.info("number of planets loaded from SWAPI : {}", planetsFetched.size());
    }

    @Override
    public List<Person> getPeopleFromSource(Query<Person> query) {
        log.info("People requested in cache with query: {}", query);

        List<Person> filteredPeople = this.retrievePeople().stream()
                .filter(query.filter())
                .sorted(query.sort() != null ? query.sort() : Comparator.comparing(Person::name))
                .skip(query.page().offset())
                .limit(query.page().limit())
                .toList();

        log.info("People returned: {} :  {}", filteredPeople.size(), filteredPeople);

        return filteredPeople;
    }

    @Override
    public List<Planet> getPlanetsFromSource(Query<Planet> query) {
        log.info("Planets requested in cache with query: {}", query);

        List<Planet> filteredPlanets = this.retrievePlanets().stream()
                .filter(query.filter())
                .sorted(query.sort() != null ? query.sort() : Comparator.comparing(Planet::name))
                .skip(query.page().offset())
                .limit(query.page().limit())
                .toList();

        log.info("Planets returned: {} : {}", filteredPlanets.size(), filteredPlanets);

        return filteredPlanets;
    }
}
