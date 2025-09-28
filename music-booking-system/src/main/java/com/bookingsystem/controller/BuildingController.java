package com.bookingsystem.controller;

import com.bookingsystem.pojo.Building;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.service.BuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/buildings")
@RestController
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping
    public Result addBuilding(@RequestBody Building building) {
        return buildingService.addBuilding(building);
    }

    @DeleteMapping("/{id}")
    public Result deleteBuilding(@PathVariable Long id) {
        return buildingService.deleteBuilding(id);
    }

    @PutMapping
    public Result updateBuilding(@RequestBody Building building) {
        return buildingService.updateBuilding(building);
    }

    @GetMapping("/{id}")
    public Result getBuildingById(@PathVariable Long id) {
        return buildingService.getBuildingById(id);
    }

    @GetMapping
    public Result getAllBuildings() {
        return buildingService.getAllBuildings();
    }
}