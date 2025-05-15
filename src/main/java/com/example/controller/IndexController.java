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
import java.util.HashMap;
import java.util.List;

//前台首页
@CrossOrigin//解决跨域问题注解
@RestController
public class IndexController {
    @Resource
    NewsDao newsDao;
    @Resource
    ImgadvDao imgadvDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    ShujuDao shujuDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ProtypeDao protypeDao;

    //查询前台首页信息
    @ResponseBody
    @RequestMapping("index")
    public Result index(HttpServletRequest request){
        HashMap<String,Object> res = new HashMap<String,Object>();
        String key1 = request.getParameter("key1");

        //查询健康资讯信息
        List<News> list = newsDao.selectAll(null);
        //查询轮播图信息
        List<Imgadv> imglist = imgadvDao.selectAll(null);
        //查询菜品信息
        List<Shuju> sjlist = shujuDao.selectAll(null);
        for (Shuju shuju : sjlist) {
            Category category = categoryDao.findById(shuju.getCategoryid());
            shuju.setCategoryname(category.getName());
            Protype protype = protypeDao.findById(shuju.getProtypeid());
            shuju.setProtypename(protype.getName());
        }
        //根据用户口味，健康状况，饮食目标推荐菜品
        String memberid = request.getParameter("memberid");
        HashMap map = new HashMap();
        if(memberid!=null){
            Member member = memberDao.findById(Integer.parseInt(memberid));
            map.put("key2",member.getProtypeid());
            map.put("jktype",member.getJktype());
            map.put("mbtype",member.getMbtype());

        List<Shuju> phlist = shujuDao.selectTJ(map);
        for (Shuju shuju1 : phlist) {
            Category category1 = categoryDao.findById(shuju1.getCategoryid());
            shuju1.setCategoryname(category1.getName());
            Protype protype = protypeDao.findById(shuju1.getProtypeid());
            shuju1.setProtypename(protype.getName());
        }
        res.put("phlist", phlist);
        }
        List<Shuju> pflist = shujuDao.selectPh(map);
        for (Shuju shuju2 : pflist) {
            Category category1 = categoryDao.findById(shuju2.getCategoryid());
            shuju2.setCategoryname(category1.getName());
            Protype protype = protypeDao.findById(shuju2.getProtypeid());
            shuju2.setProtypename(protype.getName());
        }

        res.put("sjlist", sjlist);
        res.put("pflist", pflist);
        res.put("nlist", list);
        res.put("imglist", imglist);
        return Result.success(res);
    }


}
