package com.example.controller;

import com.example.common.Result;
import com.example.dao.CategoryDao;
import com.example.entity.Category;
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
 * (Category)表控制层
 *
 */
@RestController
public class CategoryController {
    @Resource
    CategoryDao categoryDao;
    //信息列表
    @ResponseBody
    @RequestMapping("categoryList")
    public Result categoryList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryDao.selectAll(map);
        PageInfo<Category> pageInfo = new PageInfo<Category>(list);
        return Result.success(pageInfo);
    }
    
    //添加或编辑信息
    @ResponseBody
    @RequestMapping("categoryAdd")
    public Result categoryAdd(Category category , HttpServletRequest request) {
        if(category.getId()==null){
            categoryDao.add(category);
        }else{
            categoryDao.update(category);
        }
        return Result.success();
    }
    

    //删除信息
    @ResponseBody
    @RequestMapping("categoryDel")
    public Result categoryDel(int id,HttpServletRequest request) {
        categoryDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("categoryDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            categoryDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }

    //查询全部信息列表
    @ResponseBody
    @RequestMapping("cateListAll")
    public Result cateListAll(HttpServletRequest request){
        List<Category> list = categoryDao.selectAll(null);
        return Result.success(list);
    }
}

