package com.example.dao;

import com.example.entity.News;
import java.util.HashMap;
import java.util.List;

/**
 * 健康资讯信息(News)表数据库访问层
 *
 */
public interface NewsDao {

    News findById(int id);
    List<News> selectAll(HashMap map);
    void add(News news);
    void update(News news);
    void delete(int id);

}

