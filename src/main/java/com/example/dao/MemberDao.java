package com.example.dao;


import com.example.entity.Member;

import java.util.HashMap;
import java.util.List;

/**
 * 普通用户信息表(Member)表数据库访问层
 *
 * @author makejava
 */
public interface MemberDao {

    Member findById(int id);
    List<Member> selectOne(HashMap map);
    List<Member> selectAll(HashMap map);
    void add(Member member);
    void update(Member member);
    void delete(int id);

}

