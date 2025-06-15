package com.mercedes.benz.infrastructure.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPlanetsResponse {
    public List<PlanetsResponse> results;
    public String next;
}
