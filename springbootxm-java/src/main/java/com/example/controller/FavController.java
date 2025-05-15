package com.example.controller;

import com.example.common.CustomException;
import com.example.common.Result;
import com.example.dao.CategoryDao;
import com.example.dao.FavDao;
import com.example.dao.MemberDao;
import com.example.dao.ShujuDao;
import com.example.entity.Category;
import com.example.entity.Fav;
import com.example.entity.Member;
import com.example.entity.Shuju;
import com.example.util.ItemCFDemo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收藏(Fav)表控制层
 *
 */
@RestController
public class FavController {
    @Resource
    FavDao favDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ShujuDao shujuDao;
    @Resource
    CategoryDao categoryDao;

    //信息列表
    @ResponseBody
    @RequestMapping("favList")
    public Result favList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String memberid = request.getParameter("memberid");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("memberid", memberid);
        PageHelper.startPage(pageNum, pageSize);
        List<Fav> list = favDao.selectAll(map);
        for (Fav fav : list) {
            Shuju shuju = shujuDao.findById(fav.getShujuid());
            fav.setShujuname(shuju.getName());
            fav.setShuju(shuju);
        }
        PageInfo<Fav> pageInfo = new PageInfo<Fav>(list);
        return Result.success(pageInfo);
    }
    
    //添加或编辑信息
    @ResponseBody
    @RequestMapping("favAdd")
    public Result favAdd(Fav fav ,HttpServletRequest request) {
        HashMap map = new HashMap();
        map.put("memberid", fav.getMemberid());
        map.put("shujuid", fav.getShujuid());
        List<Fav> list = favDao.selectAll(map);
        if(list.size()==0){
            favDao.add(fav);
        }else{
            favDao.delete(list.get(0).getId());
            throw new CustomException("已取消收藏");
        }
        return Result.success();
    }
    //删除信息
    @ResponseBody
    @RequestMapping("favDel")
    public Result favDel(int id,HttpServletRequest request) {
        favDao.delete(id);
        return Result.success();
    }

    //协同过滤推荐（菜品信息）
    @ResponseBody
    @RequestMapping("shujuxtTJ")
    public Result shujuTJ(HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        String memberid = request.getParameter("memberid");
        if(memberid!=null) {
            Member member = memberDao.findById(Integer.parseInt(memberid));
            //用户信息列表
            HashMap ppp = new HashMap();
            List<Member> memberlist = memberDao.selectAll(null);
            //System.out.println("memberlist===="+memberlist.size());
            String[] uarray = new String[memberlist.size()];
            for (int i = 0; i < memberlist.size(); i++) {
                uarray[i] = String.valueOf(memberlist.get(i).getId());
            }

            //菜品信息
            List<Shuju> shujulist = shujuDao.selectAll(ppp);
            String[] dyarray = new String[shujulist.size()];
            for (int i = 0; i < shujulist.size(); i++) {
                dyarray[i] = String.valueOf(shujulist.get(i).getId());
            }

            //记录
            Integer[][] arr2 = new Integer[memberlist.size()][];
            int count = 0;
            //System.out.println("arr2.length=="+arr2.length);
            for (int i = 0; i < arr2.length; i++) {
                Member usermap = memberlist.get(i);

                //System.out.println("userobj" + i + "    " + usermap.getId());
                //创建一维数组
                Integer[] tmpArr = new Integer[shujulist.size()];
                //创建二维数组
                for (int j = 0; j < tmpArr.length; j++) {
                    Shuju goodmap = shujulist.get(j);
                    //查询记录
                    HashMap tjmap = new HashMap();
                    tjmap.put("memberid", usermap.getId());
                    tjmap.put("shujuid", goodmap.getId());
                    List<Fav> ddinfomaplist = favDao.selectAll(tjmap);
                    if (ddinfomaplist.size() > 0) {
                        tmpArr[j] = 1;
                    } else {
                        tmpArr[j] = 0;
                    }
                }
                arr2[i] = tmpArr;
                //System.out.println("tmpArr====" + Arrays.toString(tmpArr));
            }

            ItemCFDemo u = new ItemCFDemo();
            u.users = uarray;
            u.movies = dyarray;
            u.allUserMovieCommentList = arr2;
            u.membernum = memberlist.size();
            u.mvnum = shujulist.size();
            List<String> rtnlist = u.mvlist(String.valueOf(member.getId()));
            List<Shuju> ctlist = new ArrayList<Shuju>();
            for (int i = 0; i < rtnlist.size(); i++) {
                //System.out.println("rtnlist=========================================" + rtnlist.get(i));
                Shuju shuju = shujuDao.findById(Integer.parseInt(String.valueOf(rtnlist.get(i))));
                Category category = categoryDao.findById(shuju.getCategoryid());
                shuju.setCategoryname(category.getName());
                ctlist.add(shuju);
                //System.out.println("推荐的==="+shuju.getName());
            }
            res.put("ctlist", ctlist);
        }
        return Result.success(res);
    }
    
}

