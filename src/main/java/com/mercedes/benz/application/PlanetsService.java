package com.mercedes.benz.application;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.port.in.PlanetsQueryUseCase;
import com.mercedes.benz.domain.port.out.PlanetsDataSource;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public class PlanetsService implements PlanetsQueryUseCase {
    private final PlanetsDataSource planetsDataSource;

    public PlanetsService(PlanetsDataSource planetsDataSource) {
        this.planetsDataSource = planetsDataSource;
    }

    @Override
    public List<Planet> execute(Query<Planet> query) {
        return planetsDataSource.getPlanetsFromSource(query);
    }
}
