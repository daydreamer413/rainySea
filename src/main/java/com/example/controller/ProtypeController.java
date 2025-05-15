package com.example.controller;

import com.example.common.Result;
import com.example.dao.ProtypeDao;
import com.example.entity.Protype;
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
 * 帖子分类(Protype)表控制层
 *
 */
@RestController
public class ProtypeController {
    @Resource
    ProtypeDao protypeDao;

    //信息列表
    @ResponseBody
    @RequestMapping("protypeList")
    public Result protypeList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<Protype> list = protypeDao.selectAll(map);
        PageInfo<Protype> pageInfo = new PageInfo<Protype>(list);
        return Result.success(pageInfo);
    }

    //查询所有信息列表
    @ResponseBody
    @RequestMapping("protypeListAll")
    public Result protypeListAll(HttpServletRequest request){
        List<Protype> list = protypeDao.selectAll(null);
        return Result.success(list);
    }
    
    //添加或编辑信息
    @ResponseBody
    @RequestMapping("protypeAdd")
    public Result protypeAdd(Protype protype ,HttpServletRequest request) {
        if(protype.getId()==null){
            protypeDao.add(protype);
        }else{
            protypeDao.update(protype);
        }
        return Result.success();
    }
    

    //删除信息
    @ResponseBody
    @RequestMapping("protypeDel")
    public Result protypeDel(int id, HttpServletRequest request) {
        protypeDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("protypeDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            protypeDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }
    
}

