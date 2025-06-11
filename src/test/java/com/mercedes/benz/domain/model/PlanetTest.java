package com.mercedes.benz.domain.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetTest {
    @Test
    void shouldComparePlanets() {
        Planet p1 = new Planet("Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000", "date");
        Planet p2 = new Planet("Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000", "date");

        assertEquals(p1,p2);
    }
}
