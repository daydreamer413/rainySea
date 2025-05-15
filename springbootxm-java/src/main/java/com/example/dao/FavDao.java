package com.example.dao;

import com.example.entity.Fav;

import java.util.HashMap;
import java.util.List;

/**
 * 收藏(Fav)表数据库访问层
 *
 */
public interface FavDao {
       Fav findById(int id);
       List<Fav> selectAll(HashMap map);
       void add(Fav fav);
       void update(Fav fav);
       void delete(int id);
       

}

