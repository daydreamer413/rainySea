package com.example.controller;


import com.example.common.Result;
import com.example.dao.DianzanDao;
import com.example.dao.MemberDao;
import com.example.dao.NewsDao;
import com.example.entity.Dianzan;
import com.example.entity.News;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 健康资讯信息(News)表控制层
 *
 */
@CrossOrigin
@RestController
public class NewsController {
    @Resource
    NewsDao newsDao;
    @Resource
    MemberDao MemberDao;
    @Resource
    DianzanDao dianzanDao;
    
    //信息列表
    @ResponseBody
    @RequestMapping("newsList")
    public Result newsList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsDao.selectAll(map);
        PageInfo<News> pageInfo = new PageInfo<News>(list);
        return Result.success(pageInfo);
    }

    //添加信息
    @ResponseBody
    @RequestMapping("newsAdd")
    public Result newsAdd(News news ,HttpServletRequest request) {
        if(news.getId()==null){
            newsDao.add(news);
        }else{
            newsDao.update(news);
        }
        return Result.success();
    }

    //编辑信息
    @ResponseBody
    @RequestMapping("newsEdit")
    public Result newsEdit(News news ,HttpServletRequest request) {
        newsDao.update(news);
        return Result.success();
    }
    //查询信息详情
    @ResponseBody
    @RequestMapping("newsDateil")
    public Result newsDateil(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        News news = newsDao.findById(id);
        //查询其它资讯信息
        List<News> list = newsDao.selectAll(null);
        //查询点赞数据
        map.put("sjid", id);
        map.put("typename","资讯");
        List<Dianzan> dzlist = dianzanDao.selectAll(map);
        res.put("news",news);
        res.put("list",list);
        res.put("dzlist",dzlist);
        return Result.success(res);
    }


    //删除信息
    @ResponseBody
    @RequestMapping("newsDel")
    public Result newsDel(int id,HttpServletRequest request) {
        newsDao.delete(id);
        return Result.success();
    }


    //批量删除信息
    @ResponseBody
    @RequestMapping("newsDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            newsDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }


}

