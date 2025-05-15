package com.example.dao;

import com.example.entity.Dianzan;

import java.util.HashMap;
import java.util.List;

/**
 * 点赞(Dianzan)表数据库访问层
 *
 */
public interface DianzanDao {
       Dianzan findById(int id);
           List<Dianzan> selectAll(HashMap map);
           void add(Dianzan dianzan);
           void update(Dianzan dianzan);
           void delete(int id);
       

}

