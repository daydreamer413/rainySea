package com.example.dao;

import com.example.entity.Goods;

import java.util.HashMap;
import java.util.List;

/**
 * 食物信息(Goods)表数据库访问层
 *
 */
public interface GoodsDao {
   Goods findById(int id);
   List<Goods> selectAll(HashMap map);
   List<Goods> selectSj(HashMap map);
   void add(Goods goods);
   void update(Goods goods);
   void delete(int id);
   

}

