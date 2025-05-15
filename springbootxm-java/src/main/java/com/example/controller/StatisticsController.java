package com.example.controller;


import com.example.common.Result;
import com.example.dao.*;
import com.example.entity.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin//解决跨域问题注解
@RestController
public class StatisticsController {
    @Resource
    CategoryDao categoryDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ShujuDao shujuDao;

    //菜品评分统计{柱状图}
    @ResponseBody
    @RequestMapping("bar")
    public Result bar(HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        List<Shuju> olist = shujuDao.selectAll(map);
        ArrayList xdata = new ArrayList();
        ArrayList ydata = new ArrayList();
        for(Shuju shuju:olist){
         /*   map.put("goodsid",goods.getId());
            int slnum = 0;
            List<Ordermsg> commlist = ordermsgDao.selectAll(map);
            for(Ordermsg ordermsg:commlist){
                slnum+=ordermsg.getSlnum();
            }*/
            xdata.add(shuju.getName());
            ydata.add(shuju.getPfnum());
        }
        res.put("xdata", xdata);
        res.put("ydata", ydata);
        return Result.success(res);
    }

    //菜品分类统计
    @ResponseBody
    @RequestMapping("bie")
    public Result tjorder(HttpServletRequest request) {
        List dataval = new ArrayList();
        List<Category> list = categoryDao.selectAll(null);
        for(Category category:list){
            HashMap map1 = new HashMap();
            map1.put("key1",category.getId());
            List<Shuju> glist = shujuDao.selectAll(map1);
            map1.put("value", glist.size());
            map1.put("name", category.getName());
            dataval.add(map1);
        }
        return Result.success(dataval);
    }

}
