package com.example.controller;

import com.example.common.Result;
import com.example.dao.GoodsDao;
import com.example.entity.Goods;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 食物信息(Goods)表控制层
 *
 */
@RestController
public class GoodsController {
    @Resource
    GoodsDao goodsDao;

    //信息列表
    @ResponseBody
    @RequestMapping("goodsList")
    public Result goodsList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("key1", key1);
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsDao.selectAll(map);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(list);
        return Result.success(pageInfo);
    }

    //添加或编辑信息
    @ResponseBody
    @RequestMapping("goodsAdd")
    public Result goodsAdd(Goods goods ,HttpServletRequest request) {
        if(goods.getId()==null){
            goodsDao.add(goods);
        }else{
            goodsDao.update(goods);
        }
        return Result.success();
    }

    //是否推荐
    @ResponseBody
    @RequestMapping("goodsEdit")
    public Result goodsEdit(Goods goods ,HttpServletRequest request) {
        goodsDao.update(goods);
        return Result.success();
    }

    //删除信息
    @ResponseBody
    @RequestMapping("goodsDel")
    public Result goodsDel(int id,HttpServletRequest request) {
        goodsDao.delete(id);
        return Result.success();
    }

    //批量删除信息
    @ResponseBody
    @RequestMapping("goodsDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            goodsDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }

}

