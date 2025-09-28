package com.bookingsystem.mapper;

import com.bookingsystem.pojo.Building;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuildingMapper {

    @Insert("INSERT INTO buildings (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertBuilding(Building building);

    @Delete("DELETE FROM buildings WHERE id = #{id}")
    int deleteBuilding(Long id);

    @Update("UPDATE buildings SET name = #{name}, description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateBuilding(Building building);

    @Select("SELECT * FROM buildings WHERE id = #{id}")
    Building selectBuildingById(Long id);

    @Select("SELECT * FROM buildings")
    List<Building> selectAllBuildings();

    @Select("SELECT COUNT(*) FROM rooms WHERE building_id = #{id}")
    int selectAvailableClassrooms(Long id);
}