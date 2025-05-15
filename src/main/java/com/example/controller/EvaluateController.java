package com.example.controller;

import com.example.common.Result;
import com.example.dao.EvaluateDao;
import com.example.dao.MemberDao;
import com.example.dao.ShujuDao;
import com.example.entity.Evaluate;
import com.example.entity.Member;
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
 * 评价(Evaluate)表控制层
 *
 */
@RestController
public class EvaluateController {
    @Resource
    EvaluateDao evaluateDao;
    @Resource
    MemberDao memberDao;
    @Resource
    ShujuDao shujuDao;

    //信息列表
    @ResponseBody
    @RequestMapping("evaluateList")
    public Result evaluateList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        String shujuid = request.getParameter("shujuid");
        String evaluateid = request.getParameter("evaluateid");
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("shujuid", shujuid);
        map.put("evaluateid", evaluateid);
        PageHelper.startPage(pageNum, pageSize);
        List<Evaluate> list = evaluateDao.selectAll(map);
        for (Evaluate evaluate : list) {
            Shuju shuju = shujuDao.findById(evaluate.getShujuid());
            evaluate.setShuju(shuju);
            Member member = memberDao.findById(evaluate.getMemberid());
            evaluate.setMember(member);
            //查询二级回复列表
            HashMap hfmap = new HashMap();
            hfmap.put("evaluateid", evaluate.getId());
            List<Evaluate> hflist = evaluateDao.selectAll(hfmap);
            for (Evaluate hf : hflist) {
                Member member1 = memberDao.findById(hf.getMemberid());
                hf.setMember(member1);
            }
            evaluate.setHflist(hflist);

        }
        PageInfo<Evaluate> pageInfo = new PageInfo<Evaluate>(list);
        return Result.success(pageInfo);
    }
    
    //添加评价信息
    @ResponseBody
    @RequestMapping("evaluateAdd")
    public Result evaluateAdd(Evaluate evaluate ,HttpServletRequest request) {
        Shuju shuju = shujuDao.findById(evaluate.getShujuid());
        if(Integer.parseInt(evaluate.getEvaluateid())==0){
            if(shuju.getPfnum()>0){
                Double pf = (shuju.getPfnum()+evaluate.getPfnum())/2;
                shuju.setPfnum(pf);
            }else {
                shuju.setPfnum(evaluate.getPfnum());
            }


            shujuDao.update(shuju);
        }
        evaluateDao.add(evaluate);
        return Result.success();
    }


    //删除信息
    @ResponseBody
    @RequestMapping("evaluateDel")
    public Result evaluateDel(int id,HttpServletRequest request) {
        evaluateDao.delete(id);
        return Result.success();
    }
    
    //批量删除信息
    @ResponseBody
    @RequestMapping("evaluateDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            evaluateDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }
}

