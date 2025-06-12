package com.mercedes.benz.infrastructure.adapter.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.config.SwapiProperties;
import com.mercedes.benz.infrastructure.utils.SwapiApiUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class SwapiPeopleDataSourceAdapter implements PeopleDataSource {
    private final RestTemplate restTemplate;
    private final SwapiProperties swapiProperties;

    public SwapiPeopleDataSourceAdapter(RestTemplate restTemplate, SwapiProperties swapiProperties) {
        this.restTemplate = restTemplate;
        this.swapiProperties = swapiProperties;
    }

    @Override
    public List<Person> getPeopleFromSource(Query<Person> query) {
        String peopleUrl = swapiProperties.baseUrl() + swapiProperties.endpoints().people();
        List<Person> people = SwapiApiUtil.fetchPeople(restTemplate, peopleUrl);

        Predicate<Person> filter = query.filter() != null ? query.filter() : p -> true;
        Comparator<Person> sorter = query.sort() != null ? query.sort() : (a, b) -> 0;
        PageRequest page = query.page() != null ? query.page() : new PageRequest(0, people.size());

        return people.stream()
                .filter(filter)
                .sorted(sorter)
                .skip(page.offset())
                .limit(page.limit())
                .collect(Collectors.toList());
    }
}
