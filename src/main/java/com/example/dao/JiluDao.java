package com.example.dao;

import com.example.entity.Jilu;

import java.util.HashMap;
import java.util.List;

/**
 * 饮食记录(Jilu)表数据库访问层
 *
 */
public interface JiluDao {
       Jilu findById(int id);
           List<Jilu> selectAll(HashMap map);
           void add(Jilu jilu);
           void update(Jilu jilu);
           void delete(int id);
       
}

