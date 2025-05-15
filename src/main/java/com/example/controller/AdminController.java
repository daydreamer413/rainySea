package com.example.controller;

import com.example.common.CaptureConfig;
import com.example.common.CustomException;
import com.example.common.Result;
import com.example.dao.AdminDao;
import com.example.dao.CategoryDao;
import com.example.entity.Admin;
import com.example.util.Savesession;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 管理员信息表(Admin)表控制层
 *
 */
@CrossOrigin//解决跨域问题注解
@RestController
//@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminDao adminDao;
    @Resource
    CategoryDao categoryDao;
    @Resource
    Savesession savesession;


    //用户登录
    @ResponseBody
    @RequestMapping("adminLogin")
    public Result Login(Admin user,HttpServletRequest request,HashMap map) {
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        map.put("role", user.getRole());
        String key = request.getParameter("key");
        //判断验证码
        if(!user.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))){
            CaptchaUtil.clear(request);
            throw new CustomException("验证码错误");
        }
        List<Admin> list = adminDao.selectLogin(map);
        if(list.size()==0){
            throw new CustomException("用户名或密码错误");
        }else{
            request.getSession().setAttribute("admin", list.get(0));
        }
        //登录成功后清除正确的验证码
        CaptureConfig.CAPTURE_MAP.remove(key);
        //获取当前登录用户信息
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        //获取日志信息
        savesession.rizhi(admin.getUsername(),"管理员","登录");
        //System.out.println(admin);
        return Result.success(admin);
    }
    //用户退出登录
    @ResponseBody
    @RequestMapping("logoutAdmin")
    public Result logoutMember(String username,HttpServletRequest request) {
        savesession.rizhi(username,"管理员","退出登录");
        return null;
    }

    //用户注册
    @ResponseBody
    @RequestMapping("adminReg")
    public Result adminReg(Admin admin ,HttpServletRequest request,HashMap map) {
            map.put("username",admin.getUsername());
            List<Admin> list = adminDao.selectOne(map);
            //判断用户名的唯一性
            if (list.size()>0){
                throw new CustomException("该用户名以存在，请更换");
            }
            adminDao.add(admin);
        return Result.success();
    }

    //信息列表
    @ResponseBody
    @RequestMapping("adminList")
    public Result  adminList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        String key2 = request.getParameter("key2");
        String role = request.getParameter("role");
        //HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("key1", key1);
        map.put("key2", key2);
        map.put("role", role);
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminDao.selectAll(map);
        PageInfo<Admin> pageInfo = new PageInfo<Admin>(list);
        return Result.success(pageInfo);
    }

    //添加或编辑信息
    @ResponseBody
    @RequestMapping("adminAdd")
    public Result adminAdd(Admin admin ,HttpServletRequest request) {
        Admin user = (Admin) request.getSession().getAttribute("admin");
        if(admin.getId()==null){
            HashMap map = new HashMap();
            map.put("username",admin.getUsername());
            List<Admin> list = adminDao.selectOne(map);
            //判断用户名的唯一性
            if (list.size()>0){
                throw new CustomException("该用户名以存在，请更换");
            }
            adminDao.add(admin);
        }else{
            adminDao.update(admin);
        }
        return Result.success();
    }

    //修改密码
    @ResponseBody
    @RequestMapping("adminpassEdit")
    public Result adminpassEdit(Admin admin ,HttpServletRequest request) {
        String onlpassword = request.getParameter("onlpassword");
        String confirmPasword = request.getParameter("confirmPasword");
        if(!onlpassword.equals(admin.getPassword())){
            throw new CustomException("原密码输入错误");
        }
        admin.setPassword(confirmPasword);
        adminDao.update(admin);
        return Result.success();
    }

    //删除信息
    @ResponseBody
    @RequestMapping("adminDel")
    public Result adminDel(int id,HttpServletRequest request) {
        adminDao.delete(id);
        return Result.success();
    }

    //批量删除信息
    @ResponseBody
    @RequestMapping("adminDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            adminDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }

}

