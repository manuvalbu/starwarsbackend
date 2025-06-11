package com.mercedes.benz.domain.port.in;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public interface PeopleQueryUseCase {
    List<Person> execute(Query<Person> query);
}
