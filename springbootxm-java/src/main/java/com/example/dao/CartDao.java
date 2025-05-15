package com.example.dao;

import com.example.entity.Cart;

import java.util.HashMap;
import java.util.List;

/**
 * 饮食清单(Cart)表数据库访问层
 *
 */
public interface CartDao {
       Cart findById(int id);
       List<Cart> selectAll(HashMap map);
       void add(Cart cart);
       void update(Cart cart);
       void delete(int id);
}

