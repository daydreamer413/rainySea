package com.example.util;

import com.example.common.Result;
import com.example.dao.RizhiDao;
import com.example.entity.Rizhi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Savesession {
    @Resource
    RizhiDao rizhiDao;


    //用户行为日志
    public Result rizhi(String username, String role, String typename){
        //添加登录日志
        Rizhi rizhi = new Rizhi();
        rizhi.setUsername(username);
        rizhi.setRole(role);
        rizhi.setTypename(typename);
        rizhiDao.add(rizhi);
        return null;
}

}




