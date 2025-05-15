package com.example.dao;


import com.example.entity.Message;

import java.util.HashMap;
import java.util.List;

/**
 * 在线留言(Message)表数据库访问层
 *
 */
public interface MessageDao {
       Message findById(int id);
       List<Message> selectAll(HashMap map);
       void add(Message message);
       void update(Message message);
       void delete(int id);
       


}

