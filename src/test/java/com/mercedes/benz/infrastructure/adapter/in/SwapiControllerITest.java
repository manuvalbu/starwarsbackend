package com.mercedes.benz.infrastructure.adapter.in;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.PageRequest;
import com.mercedes.benz.domain.vo.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SwapiControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeopleDataSource peopleDataSource;

    @MockBean
    private PlanetsDataSource planetsDataSource;

    private List<Person> people;
    private List<Planet> planets;

    @BeforeEach
    void setUp() {
        people = List.of(
                new Person("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male", "Tatooine", "2014-12-09"),
                new Person("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female", "Alderaan", "2014-12-09")
        );

        planets = List.of(
                new Planet("Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000", "2014-12-09"),
                new Planet("Alderaan", "24", "364", "12500", "temperate", "1 standard", "grasslands", "40", "2000000000", "2014-12-09")
        );
    }

    @Test
    void shouldReturnAllPeople() throws Exception {
        when(peopleDataSource.getPeopleFromSource(any())).thenReturn(people);

        mockMvc.perform(get("/api/swapi/people"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Luke Skywalker"));

        ArgumentCaptor<Query<Person>> captor = ArgumentCaptor.forClass(Query.class);
        verify(peopleDataSource).getPeopleFromSource(captor.capture());

        Query<Person> query = captor.getValue();
        assertThat(query.page().offset()).isEqualTo(0);
        assertThat(query.page().limit()).isEqualTo(10);
    }

    @Test
    void shouldReturnFilteredPeople() throws Exception {
        when(peopleDataSource.getPeopleFromSource(any())).thenReturn(List.of(people.get(0)));

        mockMvc.perform(get("/api/swapi/people")
                        .param("filterField", "name")
                        .param("filterValue", "Luke Skywalker")
                        .param("filterType", "EQUALS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Luke Skywalker"));

        ArgumentCaptor<Query<Person>> captor = ArgumentCaptor.forClass(Query.class);
        verify(peopleDataSource).getPeopleFromSource(captor.capture());

        Query<Person> query = captor.getValue();
        assertThat(query.filter().test(people.get(0))).isTrue();
        assertThat(query.filter().test(people.get(1))).isFalse();
    }

    @Test
    void shouldReturnSortedPeopleDescending() throws Exception {
        when(peopleDataSource.getPeopleFromSource(any())).thenReturn(people);

        mockMvc.perform(get("/api/swapi/people")
                        .param("sortField", "name")
                        .param("descending", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Luke Skywalker"));

        ArgumentCaptor<Query<Person>> captor = ArgumentCaptor.forClass(Query.class);
        verify(peopleDataSource).getPeopleFromSource(captor.capture());

        Query<Person> query = captor.getValue();
        assertThat(query.sort().compare(people.get(1), people.get(0))).isGreaterThan(0);
    }

    @Test
    void shouldReturnPaginatedPeople() throws Exception {
        when(peopleDataSource.getPeopleFromSource(any())).thenReturn(List.of(people.get(0)));

        mockMvc.perform(get("/api/swapi/people")
                        .param("offset", "0")
                        .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        ArgumentCaptor<Query<Person>> captor = ArgumentCaptor.forClass(Query.class);
        verify(peopleDataSource).getPeopleFromSource(captor.capture());

        Query<Person> query = captor.getValue();
        assertThat(query.page()).isEqualTo(new PageRequest(0, 1));
    }

    @Test
    void shouldReturnAllPlanets() throws Exception {
        when(planetsDataSource.getPlanetsFromSource(any())).thenReturn(planets);

        mockMvc.perform(get("/api/swapi/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Tatooine"));

        ArgumentCaptor<Query<Planet>> captor = ArgumentCaptor.forClass(Query.class);
        verify(planetsDataSource).getPlanetsFromSource(captor.capture());

        Query<Planet> query = captor.getValue();
        assertThat(query.page().offset()).isEqualTo(0);
        assertThat(query.page().limit()).isEqualTo(10);
    }

    @Test
    void shouldReturnFilteredPlanets() throws Exception {
        when(planetsDataSource.getPlanetsFromSource(any())).thenReturn(List.of(planets.get(0)));

        mockMvc.perform(get("/api/swapi/planets")
                        .param("filterField", "name")
                        .param("filterValue", "Tatooine")
                        .param("filterType", "EQUALS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Tatooine"));

        ArgumentCaptor<Query<Planet>> captor = ArgumentCaptor.forClass(Query.class);
        verify(planetsDataSource).getPlanetsFromSource(captor.capture());

        Query<Planet> query = captor.getValue();
        assertThat(query.filter().test(planets.get(0))).isTrue();
        assertThat(query.filter().test(planets.get(1))).isFalse();
    }

    @Test
    void shouldReturnSortedPlanetsDescending() throws Exception {
        when(planetsDataSource.getPlanetsFromSource(any())).thenReturn(planets);

        mockMvc.perform(get("/api/swapi/planets")
                        .param("sortField", "name")
                        .param("descending", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tatooine"));

        ArgumentCaptor<Query<Planet>> captor = ArgumentCaptor.forClass(Query.class);
        verify(planetsDataSource).getPlanetsFromSource(captor.capture());

        Query<Planet> query = captor.getValue();
        assertThat(query.sort().compare(planets.get(1), planets.get(0))).isGreaterThan(0);
    }

    @Test
    void shouldReturnPaginatedPlanets() throws Exception {
        when(planetsDataSource.getPlanetsFromSource(any())).thenReturn(List.of(planets.get(0)));

        mockMvc.perform(get("/api/swapi/planets")
                        .param("offset", "0")
                        .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        ArgumentCaptor<Query<Planet>> captor = ArgumentCaptor.forClass(Query.class);
        verify(planetsDataSource).getPlanetsFromSource(captor.capture());

        Query<Planet> query = captor.getValue();
        assertThat(query.page()).isEqualTo(new PageRequest(0, 1));
    }

    @Test
    void shouldReturnValidationErrorForNegativeOffsetPeople() throws Exception {
        mockMvc.perform(get("/api/swapi/people")
                        .param("offset", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorForInvalidFilterTypePeople() throws Exception {
        mockMvc.perform(get("/api/swapi/people")
                        .param("filterField", "name")
                        .param("filterValue", "Tatooine")
                        .param("filterType", "UNKNOWN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorForInvalidOrderPeople() throws Exception {
        mockMvc.perform(get("/api/swapi/people")
                        .param("sortField", "name")
                        .param("descending", "UNKOWN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorForNegativeOffsetPlanets() throws Exception {
        mockMvc.perform(get("/api/swapi/planets")
                        .param("offset", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorForInvalidFilterTypePlanets() throws Exception {
        mockMvc.perform(get("/api/swapi/planets")
                        .param("filterField", "name")
                        .param("filterValue", "Tatooine")
                        .param("filterType", "UNKNOWN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorForInvalidOrderPlanets() throws Exception {
        mockMvc.perform(get("/api/swapi/planets")
                        .param("sortField", "name")
                        .param("descending", "UNKOWN"))
                .andExpect(status().isBadRequest());
    }
}
