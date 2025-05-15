package com.example.dao;

import com.example.entity.Shuju;

import java.util.HashMap;
import java.util.List;

/**
 *菜品信息 (Shuju)表数据库访问层
 *
 */
public interface ShujuDao {
       Shuju findById(int id);
       List<Shuju> selectAll(HashMap map);
       List<Shuju> selectPh(HashMap map);
       List<Shuju> selectTJ(HashMap map);
       void add(Shuju shuju);
       void update(Shuju shuju);
       void delete(int id);
       

}

