package com.example.dao;

import com.example.entity.Rizhi;

import java.util.HashMap;
import java.util.List;

/**
 * 行为日志(Rizhi)表数据库访问层
 *
 */
public interface RizhiDao {
       Rizhi findById(int id);
           List<Rizhi> selectAll(HashMap map);
           void add(Rizhi rizhi);
           void update(Rizhi rizhi);
           void delete(int id);
       

}

