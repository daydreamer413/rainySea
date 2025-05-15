package com.example.controller;

import com.example.common.Result;
import com.example.dao.CartDao;
import com.example.dao.GoodsDao;
import com.example.dao.JiluDao;
import com.example.dao.MemberDao;
import com.example.entity.Cart;
import com.example.entity.Goods;
import com.example.entity.Jilu;
import com.example.entity.Member;
import com.example.util.CheckCode;
import com.example.util.Info;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * 饮食记录(Jilu)表控制层
 *
 */
@RestController
public class JiluController {
    @Resource
    JiluDao jiluDao;
    @Resource
    MemberDao memberDao;
    @Resource
    CartDao cartDao;
    @Resource
    GoodsDao goodsDao;

    //信息列表
    @ResponseBody
    @RequestMapping("jiluList")
    public Result jiluList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        String memberid = request.getParameter("memberid");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("key1", key1);
        map.put("memberid", memberid);
        PageHelper.startPage(pageNum, pageSize);
        List<Jilu> list = jiluDao.selectAll(map);
        for (Jilu jilu : list) {
            Member member = memberDao.findById(jilu.getMemberid());
            jilu.setMembername(member.getUsername()+" / "+ member.getName());
        }
        PageInfo<Jilu> pageInfo = new PageInfo<Jilu>(list);
        return Result.success(pageInfo);
    }

    //添加信息
    @ResponseBody
    @RequestMapping("jiluAdd")
    public Result jiluAdd(Jilu jilu ,HttpServletRequest request) {
        CheckCode ch = new CheckCode();
        String orderno = ch.getCheckCode();
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        Double yybtotal = 0.0;
        Double subtotal = 0.0;
        String productmsg = "<div>";
        for(int i=0;i<val.length;i++){
            Cart cart = cartDao.findById(Integer.parseInt(val[i]));
            Goods goods = goodsDao.findById(cart.getGoodsid());
            //计算热量总量,营养总量
            yybtotal += goods.getYynum()*cart.getSlnum();
            subtotal += goods.getRlnum()*cart.getSlnum();
            productmsg += "<p style=margin-top: 5px;margin-bottom: 5px;><span>" + goods.getName() + "</span><span style=margin-left:10px;>" + cart.getSlnum() + "</span><span style=margin-left:10px;>"+goods.getRlnum()+"</span></p>";
            //删除饮食清单信息
            cartDao.delete(Integer.parseInt(val[i]));
        }
        productmsg += "</div>";
        String content = "";
        if(subtotal<1000){
            content = "长期极低热量饮食可能导致营养缺乏、代谢减缓及肌肉流失。建议优先选择高营养密度的食物，如深色蔬菜（菠菜、西兰花）、优质蛋白（鸡蛋、无糖希腊酸奶）和健康脂肪（牛油果、坚果），并补充复合维生素。避免完全戒断碳水，可少量摄入全谷物（燕麦、藜麦）维持基础代谢。此类饮食需在医生或营养师指导下短期执行，同时监测疲劳、脱发等信号，必要时及时调整。";
        }
        if(subtotal>=1000&&subtotal<=2000){
            content = "此区间适用于多数成年人的日常需求，需注重饮食结构优化。建议采用\"211餐盘法\"：每餐2拳蔬菜、1拳优质蛋白（鱼/豆制品）、1拳复合碳水（糙米/红薯）。增加Omega-3脂肪酸（亚麻籽、深海鱼）和发酵食品（泡菜、纳豆）改善肠道健康。配合每周150分钟中强度运动（如快走、游泳），并建立规律进食节奏（间隔3-4小时）。警惕隐形热量（含糖饮品、酱料），用草本茶代替含糖饮料。";
        }
        if(subtotal>2000){
            content = "需区分主动增肌需求与被动过量摄入。运动人群可针对性补充快碳（香蕉）+蛋白（乳清蛋白）的训后组合，非运动日适当减少碳水比例。普通人群应减少精加工食品（糕点/油炸食品），通过增加膳食纤维（奇亚籽、菌)";
        }
        jilu.setGoodsname(productmsg);
        jilu.setYybtotal(yybtotal);
        jilu.setSubtotal(subtotal);
        jilu.setContent(content);
        jiluDao.add(jilu);
        return Result.success();
    }

    //编辑信息
    @ResponseBody
    @RequestMapping("jiluEdit")
    public Result jiluEdit(Jilu jilu ,HttpServletRequest request) {
        jiluDao.update(jilu);
        return Result.success();
    }

    //删除信息
    @ResponseBody
    @RequestMapping("jiluDel")
    public Result jiluDel(int id,HttpServletRequest request) {
        jiluDao.delete(id);
        return Result.success();
    }

    //批量删除信息
    @ResponseBody
    @RequestMapping("jiluDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            jiluDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }

    //饮食记录信息导出
    //@ResponseBody
    @RequestMapping("jiluDC")
    public void jiluDC(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setCharacterEncoding("UTF-8");
        HashMap map = new HashMap();
        String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        String memberid = request.getParameter("memberid");
        map.put("key", key);
        map.put("key1", key1);
        map.put("memberid", memberid);
        List<Jilu> list = jiluDao.selectAll(map);

        //创建excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet sheet = wb.createSheet("饮食记录信息表");

        //创建标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell((short) 0).setCellValue("用户姓名");
        titleRow.createCell((short) 1).setCellValue("食物信息");
        titleRow.createCell((short) 2).setCellValue("饮食日期");
        titleRow.createCell((short) 3).setCellValue("营养量(Ug)");
        titleRow.createCell((short) 4).setCellValue("热量(Ka)");
        titleRow.createCell((short) 5).setCellValue("改善建议");
        titleRow.createCell((short) 6).setCellValue("创建日期");
        //遍历将数据放到excel列中
        for (Jilu jilu : list) {
            Member member = memberDao.findById(jilu.getMemberid());
            jilu.setMembername(member.getUsername()+" / "+ member.getName());
            String goodsname = Info.filterStrIgnoreCase(jilu.getGoodsname(),"","");
            System.out.println(goodsname);

            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
            dataRow.createCell((short) 0).setCellValue(jilu.getMembername());
            dataRow.createCell((short) 1).setCellValue(jilu.getGoodsname());
            dataRow.createCell((short) 2).setCellValue(jilu.getRqdate());
            dataRow.createCell((short) 3).setCellValue(jilu.getYybtotal());
            dataRow.createCell((short) 4).setCellValue(jilu.getSubtotal());
            dataRow.createCell((short) 5).setCellValue(jilu.getContent());
            dataRow.createCell((short) 6).setCellValue(jilu.getSavetime());
        }
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String("饮食记录信息表".getBytes(),"iso-8859-1") + ".xls");

        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
        //return Result.success();
    }

}

