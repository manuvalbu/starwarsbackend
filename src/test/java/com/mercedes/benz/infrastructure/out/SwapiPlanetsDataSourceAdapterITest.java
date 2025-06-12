package com.mercedes.benz.infrastructure.out;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.adapter.out.SwapiPlanetsDataSourceAdapter;
import com.mercedes.benz.infrastructure.config.SwapiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SwapiPlanetsDataSourceAdapterITest {
    private final SwapiPlanetsDataSourceAdapter adapter;

    @Autowired
    public SwapiPlanetsDataSourceAdapterITest(SwapiProperties swapiProperties) {
        RestTemplate restTemplate = new RestTemplate();
        this.adapter = new SwapiPlanetsDataSourceAdapter(restTemplate, swapiProperties);
    }
    @Test
    void shouldFetchPlanetsFromSwapi() {
        // Given
        Predicate<Planet> filter = planet -> planet.name().toLowerCase().contains("oo");
        Comparator<Planet> sorter = Comparator.comparing(Planet::name);
        PageRequest page = new PageRequest(0, 5);

        Query<Planet> query = new Query<>(filter, sorter, page);

        // When
        List<Planet> result = adapter.getPlanetsFromSource(query);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= 5);
        assertTrue(result.get(0).name().toLowerCase().contains("oo"));
    }
}
