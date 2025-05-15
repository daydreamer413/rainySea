package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传接口
 *
 */
//上传文件保存在springboot路径里面
@RestController
@RequestMapping("/fles")
public class FileController {

    //文件上传保存路径
    private static final String filePath = System.getProperty("user.dir")+"/file/";
    private static final String url = "http://localhost:8090/fles/";

    //文件上传
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String xzurl = "";
        synchronized (FileController.class){

            String flag = System.currentTimeMillis() + "";
            String fileName = file.getOriginalFilename();
            //

            try {
                if (!FileUtil.isDirectory(filePath)) {
                    FileUtil.mkdir(filePath);
                }
                //文件存储形式时间戳+文件名
                FileUtil.writeBytes(file.getBytes(), filePath + flag +"-"+ fileName);
                //System.out.println(fileName + "---上传成功");
                //文件下载地址
                xzurl = url + flag +"-"+ fileName;
                Thread.sleep(1L);
            } catch (Exception e) {
                //System.out.println(fileName + "---上传失败");
            }
            //System.out.println("下载地址==="+xzurl);
           // System.out.println(flag);
            return Result.success(xzurl);
        }


    }

    //获取文件
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response){
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if(StrUtil.isNotEmpty(avatar)){
                response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(avatar,"utf-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath+avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }

        }catch (Exception e){
            System.out.println("下载失败");

        }


    }

    //富文本编辑器上传图片
    @PostMapping("/wang/upload")
    public Map<String,Object> wangupload(@RequestParam MultipartFile file,@RequestParam String type){
            String flag = System.currentTimeMillis() + "";
            String fileName = file.getOriginalFilename();
            try {
                //文件存储形式时间戳+文件名
                FileUtil.writeBytes(file.getBytes(), filePath + flag +"-"+ fileName);
                Thread.sleep(1L);
            } catch (Exception e) {
                //System.out.println(fileName + "---上传失败");
            }
            Map<String,Object> resmap = new HashMap<>();
            resmap.put("errno",0);
            if(type.equals("img")){
                //resmap.put("data", CollUtil.newArrayList(Dict.create().set("url","http://localhost:8090/api/fles/" + flag)));
                resmap.put("data", CollUtil.newArrayList(Dict.create().set("url",url + flag)));
            }else{
                //resmap.put("data", Dict.create().set("url","http://localhost:8090/api/fles/" + flag));
                resmap.put("data", Dict.create().set("url",url + flag));
            }
            return resmap;
    }

}

