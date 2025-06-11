package com.mercedes.benz.domain.port.in;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public interface PlanetsQueryUseCase {
    List<Planet> execute(Query<Planet> query);
}
