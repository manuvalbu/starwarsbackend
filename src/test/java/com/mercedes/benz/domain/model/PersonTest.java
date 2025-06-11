package com.mercedes.benz.domain.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {
    @Test
    void shouldComparePersons() {
        Person p1 = new Person("Luke", "172", "77", "blond", "fair", "blue", "19BBY", "male", "home", "date");
        Person p2 = new Person("Luke", "172", "77", "blond", "fair", "blue", "19BBY", "male", "home", "date");

        assertEquals(p1,p2);
    }
}
