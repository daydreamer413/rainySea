package com.example.dao;


import com.example.entity.Category;

import java.util.HashMap;
import java.util.List;

/**
 * (Category)表数据库访问层
 *
 */
public interface CategoryDao {
   Category findById(int id);
   List<Category> selectAll(HashMap map);
   void add(Category category);
   void update(Category category);
   void delete(int id);
   
}

