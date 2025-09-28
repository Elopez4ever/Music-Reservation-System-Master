package com.bookingsystem.mapper;

import com.bookingsystem.dto.AnnQueryDTO;
import com.bookingsystem.pojo.Announcement;
import com.bookingsystem.vo.AnnQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AnnMapper {
    List<AnnQueryVo> list(AnnQueryDTO annQueryDTO);

    void save(Announcement announcement);

    @Select("select id, title, content, publisher_id, priority, status, type, start_time, end_time, view_count, created_at, updated_at from announcements where id = #{id}")
    Announcement getById(Long id);

    void update(Announcement announcement);

    void delete(Long[] ids);

    @Update("update announcements set view_count = view_count + 1 where id = #{id}")
    void updateViewCount(Long id);
}
