package com.bookingsystem.mapper;

import com.bookingsystem.dto.UserQueryDTO;
import com.bookingsystem.pojo.User;
import com.bookingsystem.vo.UserQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserQueryVo> list(UserQueryDTO userQueryDTO);

    @Select("select id,username,password, real_name, student_id, email, phone, user_type, department_id, avatar_url, status, last_login_time, last_login_ip, created_at, updated_at from users where id = #{id}")
    User getById(Long id);

    @Update("update users set password = #{newPassword} where id = #{id}")
    void resetPassword(Long id,String newPassword);

    void delete(Long[] ids);

    @Update("update users set status = #{status} where id = #{id}")
    void setStatus(Integer status, Long id);


    User selectByUsername(@Param("username") String username);

    @Select("select count(*) from users")
    int getUserNum();

    void update(User user);

    void insert(User user);


    List<UserQueryVo> selectAdmins(UserQueryDTO userQueryDTO);
}
