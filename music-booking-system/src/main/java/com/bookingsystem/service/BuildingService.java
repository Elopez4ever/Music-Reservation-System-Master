package com.bookingsystem.service;

import com.bookingsystem.pojo.Building;
import com.bookingsystem.pojo.Result;

public interface BuildingService {
    Result addBuilding(Building building);
    Result deleteBuilding(Long id);
    Result updateBuilding(Building building);
    Result getBuildingById(Long id);
    Result getAllBuildings();
}