package com.mercedes.benz.infrastructure.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPeopleResponse {
    public List<PeopleResponse> results;
    public String next;
}
