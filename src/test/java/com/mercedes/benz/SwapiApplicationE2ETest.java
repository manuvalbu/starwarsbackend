package com.mercedes.benz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = SwapiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class SwapiApplicationE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${rest.base-url}")
    private String restBaseUrl;

    @Value("${rest.endpoints.people}")
    private String restEndpointPeople;

    @Value("${rest.endpoints.planets}")
    private String restEndpointPlanets;

    private String restUrlPeople;
    private String restUrlPlanets;

    @BeforeEach
    void setup() {
        restUrlPeople = restBaseUrl + restEndpointPeople;
        restUrlPlanets = restBaseUrl + restEndpointPlanets;
    }

    @Test
    void shouldReturnPeopleFromSwapi() throws Exception {
        mockMvc.perform(get(restUrlPeople))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", not(emptyOrNullString())));
    }

    @Test
    void shouldReturnPlanetsFromSwapi() throws Exception {
        mockMvc.perform(get(restUrlPlanets))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", not(emptyOrNullString())));
    }

    @Test
    void shouldFilterPeopleByName() throws Exception {
        mockMvc.perform(get(restUrlPeople)
                        .param("filterField", "name")
                        .param("filterValue", "Luke Skywalker")
                        .param("filterType", "EQUALS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", containsString("Luke")));
    }

    @Test
    void shouldSortPlanetsDescendingByName() throws Exception {
        mockMvc.perform(get(restUrlPlanets)
                        .param("sortField", "name")
                        .param("descending", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> planets = mapper.readValue(json, new TypeReference<>() {});

                    List<String> names = planets.stream()
                            .map(p -> (String) p.get("name"))
                            .toList();

                    List<String> sorted = new ArrayList<>(names);
                    sorted.sort(Comparator.reverseOrder());

                    assertEquals(sorted, names, "Names should be sorted in descending order");
                });
    }
}
