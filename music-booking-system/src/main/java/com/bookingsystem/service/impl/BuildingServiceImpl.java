package com.bookingsystem.service.impl;

import com.bookingsystem.mapper.BuildingMapper;
import com.bookingsystem.pojo.Building;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.service.BuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public Result addBuilding(Building building) {
        building.setCreatedAt(LocalDateTime.now());
        building.setUpdatedAt(LocalDateTime.now());
        int result = buildingMapper.insertBuilding(building);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.error("Failed to add building");
        }
    }

    @Override
    public Result deleteBuilding(Long id) {
        //获取该id下的教室数量
        if (buildingMapper.selectAvailableClassrooms(id) > 0) {
            return Result.error("该教学楼下存在教室，请删除教室在执行此操作！");
        }
        int result = buildingMapper.deleteBuilding(id);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.error("Failed to delete building");
        }
    }

    @Override
    public Result updateBuilding(Building building) {
        building.setUpdatedAt(LocalDateTime.now());
        int result = buildingMapper.updateBuilding(building);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.error("Failed to update building");
        }
    }

    @Override
    public Result getBuildingById(Long id) {
        Building building = buildingMapper.selectBuildingById(id);
        if (building != null) {
            return Result.success(building);
        } else {
            return Result.error("Building not found");
        }
    }

    @Override
    public Result getAllBuildings() {
        return Result.success(buildingMapper.selectAllBuildings());
    }
}