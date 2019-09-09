package com.parkbobo.controller;

import com.parkbobo.service.AppVersionMobileService;
import com.system.model.AppVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AppVersionController
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/27 10:34
 * @Version 1.0
 **/
@Controller
public class AppVersionController {
    @Resource(name = "appVersionMobileService")
    private AppVersionMobileService appVersionService;
    @RequestMapping("update")
    @ResponseBody
    public Map<String, Object> update(@RequestParam("type") String type, HttpServletRequest request, HttpServletResponse response){
        //主机ip+端口
        String contentPath = "http://"+request.getServerName()+":"+request.getServerPort()+"/"+request.getServletContext().getContextPath();
        response.setContentType("text/html;charset=utf-8");
        List<AppVersion> appVersions = this.appVersionService.getByHql("From AppVersion as a where a.isDel = 0 and a.type ="+type+" order by a.posttime desc");
        AppVersion version = appVersions.get(0);

        Map<String, Object> map=new HashMap<String,Object>();
        map.put("versionCode",version.getVersioncode());
        map.put("downUrl",contentPath+"/download?versionCode=" + version.getVersioncode());
        map.put("versionName",version.getName());
        map.put("postTime",version.getPosttime().getTime());
        map.put("content",version.getContent());
        map.put("needUpdate",version.getNeedUpdate());

        //版本号
     /*       StringBuilder s = new StringBuilder();
            s.append("{");
            s.append("\"versionCode\":\"" + version.getVersioncode() + "\",");
            s.append("\"downUrl\":\"" +  contentPath+"/download?versionCode=" + version.getVersioncode() +"\",");
            s.append("\"versionName\":\"" + version.getName() + "\",");
            s.append("\"postTime\":\"" + version.getPosttime().getTime() + "\",");
            s.append("\"content\":\"" + version.getContent() + "\",");
            s.append("\"needUpdate\":\"" + version.getNeedUpdate() + "\"");
            s.append("}");
            return s.toString();*/

     return map;
        }

    //通过版本号来下载APP
    @RequestMapping("download")
    @ResponseBody
    public ModelAndView download(@RequestParam("versionCode") String versionCode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //通过版本号来得到下载路径
        AppVersion appVersion = appVersionService.getById(versionCode);
        response.setContentType("text/html;charset=utf-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        String downLoadPath = appVersion.getAttached();
        System.out.println(downLoadPath);
        try {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(appVersion.getName().getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            appVersion.setDownloadcount(appVersion.getDownloadcount() + 1);
            appVersionService.save(appVersion);
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }

}

