package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Person;
import com.mercedes.benz.domain.port.in.PeopleQueryUseCase;
import com.mercedes.benz.domain.port.out.PeopleDataSource;
import com.mercedes.benz.domain.vo.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PeopleService implements PeopleQueryUseCase {
    private static final Logger log = LoggerFactory.getLogger(PeopleService.class);

    private final PeopleDataSource peopleDataSource;

    public PeopleService(PeopleDataSource peopleDataSource) {
        this.peopleDataSource = peopleDataSource;
    }

    @Override
    public List<Person> execute(Query<Person> query) {
        log.info("Executing people service with query: {}", query);
        return peopleDataSource.getPeopleFromSource(query);
    }
}
