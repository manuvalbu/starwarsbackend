package com.mercedes.benz.infrastructure.adapter.in;

import com.mercedes.benz.application.PeopleService;
import com.mercedes.benz.application.PlanetsService;
import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import com.mercedes.benz.infrastructure.adapter.in.model.FilterType;
import com.mercedes.benz.infrastructure.utils.QueryBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/swapi")
@Validated
@Tag(name = "Swapi API", description = "Operations related to Star Wars People and Planets")
public class SwapiController {

    private final PeopleService peopleService;
    private final PlanetsService planetsService;

    public SwapiController(PeopleService peopleService, PlanetsService planetsService) {
        this.peopleService = peopleService;
        this.planetsService = planetsService;
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getPeople(
            @RequestParam(required = false)
            @Parameter(description = "Field to filter by") String filterField,

            @RequestParam(required = false)
            @Parameter(description = "Value to filter for") String filterValue,

            @RequestParam(defaultValue = "CONTAINS")
            @Parameter(description = "Filter type") FilterType filterType,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Enable case-sensitive filtering") boolean caseSensitive,

            @RequestParam(required = false)
            @Parameter(description = "Field to sort by") String sortField,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Sort descending if true") boolean descending,

            @RequestParam(defaultValue = "0")
            @Min(0)
            @Parameter(description = "Pagination offset") int offset,

            @RequestParam(defaultValue = "10")
            @Min(1)
            @Parameter(description = "Pagination limit") int limit
    ) {
        Predicate<Person> filter = (filterField != null && filterValue != null)
                ? QueryBuilder.createPredicate(filterField, filterValue, filterType, caseSensitive)
                : person -> true;

        Comparator<Person> sort = (sortField != null)
                ? QueryBuilder.createComparator(sortField, descending)
                : Comparator.comparing(Person::name);

        Query<Person> query = new Query<>(filter, sort, new PageRequest(offset, limit));
        return ResponseEntity.ok(peopleService.execute(query));
    }

    @GetMapping("/planets")
    public ResponseEntity<List<Planet>> getPlanets(
            @RequestParam(required = false)
            @Parameter(description = "Field to filter by") String filterField,

            @RequestParam(required = false)
            @Parameter(description = "Value to filter for") String filterValue,

            @RequestParam(defaultValue = "CONTAINS")
            @Parameter(description = "Filter type") FilterType filterType,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Enable case-sensitive filtering") boolean caseSensitive,

            @RequestParam(required = false)
            @Parameter(description = "Field to sort by") String sortField,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Sort descending if true") boolean descending,

            @RequestParam(defaultValue = "0")
            @Min(0)
            @Parameter(description = "Pagination offset") int offset,

            @RequestParam(defaultValue = "10")
            @Min(1)
            @Parameter(description = "Pagination limit") int limit
    ) {
        Predicate<Planet> filter = (filterField != null && filterValue != null)
                ? QueryBuilder.createPredicate(filterField, filterValue, filterType, caseSensitive)
                : planet -> true;

        Comparator<Planet> sort = (sortField != null)
                ? QueryBuilder.createComparator(sortField, descending)
                : Comparator.comparing(Planet::name);

        Query<Planet> query = new Query<>(filter, sort, new PageRequest(offset, limit));
        return ResponseEntity.ok(planetsService.execute(query));
    }
}
