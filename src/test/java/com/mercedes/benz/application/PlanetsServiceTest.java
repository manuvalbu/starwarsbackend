package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.Query;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PlanetsServiceTest {
    @Test
    void shouldReturnPlanetsFromDataSource() {
        // Given
        PlanetsDataSource mockDataSource = mock(PlanetsDataSource.class);
        PlanetsService service = new PlanetsService(mockDataSource);

        Planet tatooine = new Planet("Tatooine", "23", "304", "10465", "arid", "1 standard", "desert", "1", "200000", "date");
        Query<Planet> dummyQuery = new Query<>(null, null, null);

        when(mockDataSource.getPlanetsFromSource(dummyQuery)).thenReturn(List.of(tatooine));

        // When
        List<Planet> result = service.execute(dummyQuery);

        // Then
        assertEquals(1, result.size());
        assertEquals("Tatooine", result.get(0).name());
        verify(mockDataSource).getPlanetsFromSource(dummyQuery);
    }
}
