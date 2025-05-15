package com.example.controller;


import com.example.common.Result;
import com.example.dao.MemberDao;
import com.example.dao.MessageDao;
import com.example.entity.Member;
import com.example.entity.Message;
import com.example.util.Info;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 在线留言(Message)表控制层
 *
 */
@CrossOrigin//解决跨域问题
@RestController
public class MessageController {
    @Resource
    MessageDao messageDao;
    @Resource
    MemberDao memberDao;
    
     //信息列表
         @ResponseBody
         @RequestMapping("messageList")
         public Result messageList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
             String key = request.getParameter("key");
             String memberid = request.getParameter("memberid");
             String mjmemberid = request.getParameter("mjmemberid");
             HashMap map = new HashMap();
             map.put("key", key);
             map.put("memberid", memberid);
             map.put("mjmemberid", mjmemberid);
             PageHelper.startPage(pageNum, pageSize);
             List<Message> list = messageDao.selectAll(map);
             for (Message message : list) {
                 Member member = memberDao.findById(message.getMemberid());
                 message.setUsername(member.getUsername()+" / "+member.getName());
                 message.setMember(member);
             }
             PageInfo<Message> pageInfo = new PageInfo<Message>(list);
             return Result.success(pageInfo);
         }
     
         //添加或编辑信息
         @ResponseBody
         @RequestMapping("messageAdd")
         public Result messageAdd(Message message ,HttpServletRequest request) {
             if(message.getId()==null){
                 messageDao.add(message);
             }else{
                 message.setHftime(Info.getDateStr());
                 messageDao.update(message);
             }
             return Result.success();
         }

    //留言回复
    @ResponseBody
    @RequestMapping("messageEdit")
    public Result messageEdit(Message message ,HttpServletRequest request) {
        message.setHftime(Info.getDateStr());
        messageDao.update(message);
        return Result.success();
    }
     

         //删除信息
         @ResponseBody
         @RequestMapping("messageDel")
         public Result messageDel(int id, HttpServletRequest request) {
             messageDao.delete(id);
             return Result.success();
         }
     

 //批量删除信息
    @ResponseBody
    @RequestMapping("messageDeleteAll")
    public Result typeDeleteAll(HttpServletRequest request) {
        String vals = request.getParameter("vals");
        String[] val = vals.split(",");
        for(int i=0;i<val.length;i++){
            messageDao.delete(Integer.parseInt(val[i]));
        }
        return Result.success();
    }
}

