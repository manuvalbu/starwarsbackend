package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.port.in.PeopleQueryUseCase;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public class PeopleService implements PeopleQueryUseCase {
    private final PeopleDataSource peopleDataSource;

    public PeopleService(PeopleDataSource peopleDataSource) {
        this.peopleDataSource = peopleDataSource;
    }

    @Override
    public List<Person> execute(Query<Person> query) {
        return peopleDataSource.getPeopleFromSource(query);
    }
}
