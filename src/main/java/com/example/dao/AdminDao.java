package com.example.dao;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 管理员信息表(Admin)表数据库访问层
 *
 */
@Repository
public interface AdminDao  {

    Admin findById(int id);
    List<Admin> selectLogin(HashMap map);
    List<Admin> selectOne(HashMap map);
    List<Admin> selectAll(HashMap map);
    void add(Admin admin);
    void update(Admin admin);
    void delete(int id);
}

