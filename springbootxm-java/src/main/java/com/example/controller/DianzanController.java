package com.example.controller;

import com.example.common.CustomException;
import com.example.common.Result;
import com.example.dao.DianzanDao;
import com.example.dao.MemberDao;
import com.example.dao.ShujuDao;
import com.example.entity.Dianzan;
import com.example.entity.Shuju;
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
 * 点赞(Dianzan)表控制层
 *
 */
@RestController
public class DianzanController {
    @Resource
    DianzanDao dianzanDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ShujuDao shujuDao;

    //信息列表
    @ResponseBody
    @RequestMapping("dianzanList")
    public Result dianzanList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String memberid = request.getParameter("memberid");
        String shujuid = request.getParameter("shujuid");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("memberid", memberid);
        map.put("shujuid", shujuid);
        PageHelper.startPage(pageNum, pageSize);
        List<Dianzan> list = dianzanDao.selectAll(map);
        for (Dianzan dianzan : list) {
            Shuju shuju = shujuDao.findById(dianzan.getShujuid());
            dianzan.setShujuname(shuju.getName());
        }
        PageInfo<Dianzan> pageInfo = new PageInfo<Dianzan>(list);
        return Result.success(pageInfo);
    }
    
    //添加或编辑信息
    @ResponseBody
    @RequestMapping("dianzanAdd")
    public Result dianzanAdd(Dianzan dianzan ,HttpServletRequest request) {
        HashMap map = new HashMap();
        map.put("memberid", dianzan.getMemberid());
        map.put("shujuid", dianzan.getShujuid());
        List<Dianzan> list = dianzanDao.selectAll(map);
        if(list.size()==0){
            dianzanDao.add(dianzan);
        }else{
            dianzanDao.delete(list.get(0).getId());
            throw new CustomException("已取消点赞");
        }
        return Result.success();
    }
    

    //删除信息
    @ResponseBody
    @RequestMapping("dianzanDel")
    public Result dianzanDel(int id,HttpServletRequest request) {
        dianzanDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("dianzanDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            dianzanDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }

    //查询是否对当前数据信息点赞
    @ResponseBody
    @RequestMapping("DzList")
    public Result DzList(HttpServletRequest request){
        String memberid = request.getParameter("memberid");
        String shujuid = request.getParameter("shujuid");
        HashMap map = new HashMap();
        map.put("memberid", memberid);
        map.put("shujuid", shujuid);
        List<Dianzan> list = dianzanDao.selectAll(map);
        return Result.success(list);
    }


}

