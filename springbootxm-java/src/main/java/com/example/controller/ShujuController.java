package com.example.controller;

import com.example.common.Result;
import com.example.dao.*;
import com.example.entity.*;
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
 * 菜品信息(Shuju)表控制层
 *
 */
@RestController
public class ShujuController {
    @Resource
    ShujuDao shujuDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    DianzanDao dianzanDao;
    @Resource
    FavDao favDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ProtypeDao protypeDao;

    //信息列表
    @ResponseBody
    @RequestMapping("shujuList")
    public Result shujuList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        String key2 = request.getParameter("key2");
        String memberid = request.getParameter("memberid");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("key1", key1);
        map.put("key2", key2);
        map.put("memberid", memberid);
        PageHelper.startPage(pageNum, pageSize);
        List<Shuju> list = shujuDao.selectAll(map);
        for (Shuju shuju : list) {
            Category category = categoryDao.findById(shuju.getCategoryid());
            shuju.setCategoryname(category.getName());
            Protype protype = protypeDao.findById(shuju.getProtypeid());
            shuju.setProtypename(protype.getName());
            Member member = memberDao.findById(shuju.getMemberid());
            shuju.setMembername(member.getUsername()+" / "+member.getName());
        }
        PageInfo<Shuju> pageInfo = new PageInfo<Shuju>(list);
        return Result.success(pageInfo);
    }

    //查询全部信息列表
    @ResponseBody
    @RequestMapping("shujuListAll")
    public Result shujuListAll(HttpServletRequest request){
        HashMap map = new HashMap();
        List<Shuju> list = shujuDao.selectAll(map);
        return Result.success(list);
    }
    
    //添加或编辑信息
    @ResponseBody
    @RequestMapping("shujuAdd")
    public Result shujuAdd(Shuju shuju ,HttpServletRequest request) {
        if(shuju.getId()==null){
            shujuDao.add(shuju);
        }else{
            shujuDao.update(shuju);
        }
        return Result.success();
    }
    
    //查询信息详情
    @ResponseBody
    @RequestMapping("shujuDateil")
    public Result shujuDateil(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        Shuju shuju = shujuDao.findById(id);
        Category category = categoryDao.findById(shuju.getCategoryid());
        shuju.setCategoryname(category.getName());
        Member member = memberDao.findById(shuju.getMemberid());
        shuju.setMembername(member.getName());
        Protype protype = protypeDao.findById(shuju.getProtypeid());
        shuju.setProtypename(protype.getName());
        //添加浏览量
        shuju.setLlnum(shuju.getLlnum()+1);
        shujuDao.update(shuju);

        //查询点赞或收藏数据
        map.put("shujuid", id);
        List<Dianzan> dzlist = dianzanDao.selectAll(map);
        List<Fav> falist = favDao.selectAll(map);
        shuju.setDznum(dzlist.size());
        shuju.setFanum(falist.size());

        //查询排行信息
        List<Shuju> list = shujuDao.selectPh(null);
        for (Shuju shuju1 : list) {
            Category category1 = categoryDao.findById(shuju1.getCategoryid());
            shuju1.setCategoryname(category1.getName());
            Protype protype1 = protypeDao.findById(shuju1.getProtypeid());
            shuju1.setProtypename(protype1.getName());
        }
        res.put("dzlist",dzlist);
        res.put("shuju",shuju);
        res.put("list",list);
        return Result.success(res);
    }
    
    //删除信息
    @ResponseBody
    @RequestMapping("shujuDel")
    public Result shujuDel(int id, HttpServletRequest request) {
        shujuDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("shujuDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            shujuDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }
}

