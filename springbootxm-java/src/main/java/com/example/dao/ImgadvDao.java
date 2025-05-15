package com.example.dao;


import com.example.entity.Imgadv;

import java.util.HashMap;
import java.util.List;

/**
 * 轮播图(Imgadv)表数据库访问层
 *
 */
public interface ImgadvDao {
    Imgadv findById(int id);
    List<Imgadv> selectAll(HashMap map);
    void add(Imgadv imgadv);
    void update(Imgadv imgadv);
    void delete(int id);

}

