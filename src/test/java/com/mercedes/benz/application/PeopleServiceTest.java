package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.vo.Query;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PeopleServiceTest {
    @Test
    void shouldReturnPeopleFromDataSource() {
        // Given
        PeopleDataSource mockDataSource = mock(PeopleDataSource.class);
        PeopleService service = new PeopleService(mockDataSource);

        Person luke = new Person("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male", "Tatooine", "date");
        Query<Person> dummyQuery = new Query<>(null, null, null);

        when(mockDataSource.getPeopleFromSource(dummyQuery)).thenReturn(List.of(luke));

        // When
        List<Person> result = service.execute(dummyQuery);

        // Then
        assertEquals(1, result.size());
        assertEquals("Luke Skywalker", result.get(0).name());
        verify(mockDataSource).getPeopleFromSource(dummyQuery);
    }
}
