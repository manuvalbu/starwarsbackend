package com.mercedes.benz.domain.port.out;

import com.mercedes.benz.domain.model.Planet;
import com.mercedes.benz.domain.vo.Query;
import java.util.List;

public interface PlanetsDataSource {
    List<Planet> getPlanetsFromSource(Query<Planet> query);
}
