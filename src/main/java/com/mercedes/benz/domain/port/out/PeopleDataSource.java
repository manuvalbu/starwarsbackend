package com.mercedes.benz.domain.port.out;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public interface PeopleDataSource {
    List<Person> getPeopleFromSource(Query<Person> query);
}
