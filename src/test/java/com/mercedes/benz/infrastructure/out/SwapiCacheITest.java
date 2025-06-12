package com.mercedes.benz.infrastructure.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.adapter.out.SwapiCacheAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SwapiCacheITest {

    @Autowired
    private SwapiCacheAdapter adapter;

    @BeforeEach
    void setup() {
        adapter.loadDataFromSwapi();
    }

    @Test
    void shouldLoadPeopleAndFilterWithQuery() {
        // Given
        Predicate<Person> filter = person -> person.name().toLowerCase().contains("l");
        Comparator<Person> sorter = Comparator.comparing(Person::name);
        PageRequest page = new PageRequest(0, 10);
        Query<Person> query = new Query<>(filter, sorter, page);

        // When
        List<Person> result = adapter.getPeopleFromSource(query);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= 10);
        assertTrue(result.get(0).name().toLowerCase().contains("l"));
    }

    @Test
    void shouldLoadPlanetsAndFilterWithQuery() {
        // Given
        Predicate<Planet> filter = Planet -> Planet.name().toLowerCase().contains("m");
        Comparator<Planet> sorter = Comparator.comparing(Planet::name);
        PageRequest page = new PageRequest(0, 10);
        Query<Planet> query = new Query<>(filter, sorter, page);

        // When
        List<Planet> result = adapter.getPlanetsFromSource(query);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= 10);
        assertTrue(result.get(0).name().toLowerCase().contains("m"));
    }

    @Test
    void shouldHaveCachedDataAfterLoading() {
        List<Person> people = adapter.getPeople();
        assertNotNull(people);
        assertFalse(people.isEmpty(), "People list should not be empty after loading");
    }
}

