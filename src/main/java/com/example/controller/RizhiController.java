package com.example.controller;

import com.example.common.Result;
import com.example.dao.MemberDao;
import com.example.dao.RizhiDao;
import com.example.entity.Rizhi;
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
 * 行为日志(Rizhi)表控制层
 *
 */
@RestController
public class RizhiController {
    @Resource
    RizhiDao rizhiDao;
    @Resource
    MemberDao memberDao;

    //信息列表
    @ResponseBody
    @RequestMapping("rizhiList")
    public Result rizhiList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<Rizhi> list = rizhiDao.selectAll(map);
        PageInfo<Rizhi> pageInfo = new PageInfo<Rizhi>(list);
        return Result.success(pageInfo);
    }

    //删除信息
    @ResponseBody
    @RequestMapping("rizhiDel")
    public Result rizhiDel(int id,HttpServletRequest request) {
        rizhiDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("rizhiDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            rizhiDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }
}

