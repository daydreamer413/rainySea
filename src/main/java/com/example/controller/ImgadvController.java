package com.example.controller;


import com.example.common.Result;
import com.example.dao.ImgadvDao;
import com.example.entity.Imgadv;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 轮播图(Imgadv)表控制层
 *
 */
@CrossOrigin
@RestController
public class ImgadvController {
    @Resource
    ImgadvDao imgadvDao;

    //信息列表
    @ResponseBody
    @RequestMapping("imgadvList")
    public Result imgadvList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<Imgadv> list = imgadvDao.selectAll(map);
        PageInfo<Imgadv> pageInfo = new PageInfo<Imgadv>(list);
        return Result.success(pageInfo);
    }



    //添加或编辑信息
    @ResponseBody
    @RequestMapping("imgadvAdd")
    public Result imgadvAdd(Imgadv imgadv ,HttpServletRequest request) {
        if(imgadv.getId()==null){
            imgadvDao.add(imgadv);
        }else{
            imgadvDao.update(imgadv);
        }
        return Result.success();
    }

    //删除信息
    @ResponseBody
    @RequestMapping("imgadvDel")
    public Result imgadvDel(int id,HttpServletRequest request) {
        imgadvDao.delete(id);
        return Result.success();
    }

    //批量删除信息
    @ResponseBody
    @RequestMapping("imgadvDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            imgadvDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }


}

