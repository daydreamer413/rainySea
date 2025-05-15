package com.example.dao;

import com.example.entity.Protype;

import java.util.HashMap;
import java.util.List;

/**
 * 帖子分类(Protype)表数据库访问层
 *
 */
public interface ProtypeDao {
   Protype findById(int id);
       List<Protype> selectAll(HashMap map);
       void add(Protype protype);
       void update(Protype protype);
       void delete(int id);

}

