package com.example.dao;

import com.example.entity.Evaluate;

import java.util.HashMap;
import java.util.List;

/**
 * 评价(Evaluate)表数据库访问层
 *
 */
public interface EvaluateDao {
       Evaluate findById(int id);
       List<Evaluate> selectAll(HashMap map);
       void add(Evaluate evaluate);
       void update(Evaluate evaluate);
       void delete(int id);
       

}

