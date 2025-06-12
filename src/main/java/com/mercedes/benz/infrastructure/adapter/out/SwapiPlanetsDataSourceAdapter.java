package com.mercedes.benz.infrastructure.adapter.out;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.config.SwapiProperties;
import com.mercedes.benz.infrastructure.utils.SwapiApiUtil;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SwapiPlanetsDataSourceAdapter implements PlanetsDataSource {
    private final RestTemplate restTemplate;
    private final SwapiProperties swapiProperties;

    public SwapiPlanetsDataSourceAdapter(RestTemplate restTemplate, SwapiProperties swapiProperties) {
        this.restTemplate = restTemplate;
        this.swapiProperties = swapiProperties;
    }

    @Override
    public List<Planet> getPlanetsFromSource(Query<Planet> query) {
        String planetsUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().planets();
        List<Planet> planets = SwapiApiUtil.fetchPlanets(restTemplate, planetsUrl);

        Predicate<Planet> filter = query.filter() != null ? query.filter() : p -> true;
        Comparator<Planet> sorter = query.sort() != null ? query.sort() : (a, b) -> 0;
        PageRequest page = query.page() != null ? query.page() : new PageRequest(0, planets.size());

        return planets.stream()
                .filter(filter)
                .sorted(sorter)
                .skip(page.offset())
                .limit(page.limit())
                .collect(Collectors.toList());
    }
}
