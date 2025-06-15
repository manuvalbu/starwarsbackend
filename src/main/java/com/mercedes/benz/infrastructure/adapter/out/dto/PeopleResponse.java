package com.mercedes.benz.infrastructure.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercedes.benz.domain.model.Person;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeopleResponse {
    public String name;
    public String height;
    public String mass;
    public String hair_color;
    public String skin_color;
    public String eye_color;
    public String birth_year;
    public String gender;
    public String homeworld;
    public String created;

    public Person mapToPerson() {
        return new Person(name, height, mass, hair_color, skin_color, eye_color, birth_year, gender, homeworld, created);
    }
}
