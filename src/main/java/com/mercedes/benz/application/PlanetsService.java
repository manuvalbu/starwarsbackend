package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.in.PlanetsQueryUseCase;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanetsService implements PlanetsQueryUseCase {
    private static final Logger log = LoggerFactory.getLogger(PlanetsService.class);

    private final PlanetsDataSource planetsDataSource;

    public PlanetsService(PlanetsDataSource planetsDataSource) {
        this.planetsDataSource = planetsDataSource;
    }

    @Override
    public List<Planet> execute(Query<Planet> query) {
        log.info("Executing planet service with query: {}", query);
        return planetsDataSource.getPlanetsFromSource(query);
    }
}
