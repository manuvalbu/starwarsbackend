package com.mercedes.benz.infrastructure.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.adapter.out.SwapiPeopleDataSourceAdapter;
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
public class SwapiPeopleDataSourceAdapterITest {
    private final SwapiPeopleDataSourceAdapter adapter;

    @Autowired
    public SwapiPeopleDataSourceAdapterITest(SwapiProperties swapiProperties) {
        RestTemplate restTemplate = new RestTemplate();
        this.adapter = new SwapiPeopleDataSourceAdapter(restTemplate, swapiProperties);
    }

    @Test
    void shouldFetchPeopleFromSwapi() {
        // Given
        Predicate<Person> filter = person -> person.name().toLowerCase().contains("sky");
        Comparator<Person> sorter = Comparator.comparing(Person::name);
        PageRequest page = new PageRequest(0, 5);

        Query<Person> query = new Query<>(filter, sorter, page);

        // When
        List<Person> result = adapter.getPeopleFromSource(query);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= 5);
        assertTrue(result.get(0).name().toLowerCase().contains("sky"));
    }
}
