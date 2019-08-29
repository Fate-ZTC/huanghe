package com.system.controller;

import com.parkbobo.utils.PageBean;
import com.system.model.AppVersion;
import com.system.service.AppVersionService;
import com.system.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

/**
 * APP管理
 * @author zj
 *@version 1.0
 */
@Controller
public class AppManagerController {


    @Resource
    private AppVersionService appVersionService;
    /**
     * APP列表
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("appManagerList")
    public ModelAndView list(Integer type,String name, Integer page, Integer pageSize) throws UnsupportedEncodingException
    {
        ModelAndView mv = new ModelAndView();
        String hql = "from AppVersion as a where a.isDel=0";
        if(type!=null && type != -1){

                hql += " and a.type ="+type;
        }
        if(name!=null&&!name.equals("")){
//        if(StringUtil.isNotEmpty(name)){
            hql += " and a.versioncode like '%"+name+"%' or (a.name like '%"+name+"%' and a.isDel=0) or (a.content like '%"+name+"%' and a.isDel=0)";
        }
        hql += " order by a.posttime desc ";
//        PageBean<AppVersion> appVersionPage = this.appVersionService.getByHql(hql, pageSize==null?12:pageSize, page==null?1:page);
        PageBean<AppVersion> appVersionPage = this.appVersionService.loadPage(hql, pageSize==null?12:pageSize, page==null?1:page);
//        com.parkbobo.utils.PageBean<OptLogs> optLogsPage = this.optLogsService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
        mv.addObject("appVersionPage", appVersionPage);
        mv.addObject("type",type);
        mv.addObject("name",name);
        mv.setViewName("manager/system/appVersion/appVersion-list");
        return mv;
    }
    /**
     * APP列表
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("appVersion_add")
//    @RequestMapping(value = "appVersion_add", method= RequestMethod.POST)
    public ModelAndView add(String method, AppVersion appVersion, HttpSession session, HttpServletRequest request) throws IOException
    {
        ModelAndView mv = new ModelAndView();
        //添加
        if(StringUtil.isNotEmpty(method) && method.equals("add"))
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("upload");

            String uploadFileName=file.getOriginalFilename();
            System.out.println("上传的文件名称:" + file.getOriginalFilename());
            String savePath = session.getServletContext().getRealPath("/upload/app/");

            File dirFile = new File(savePath);
            if (!dirFile.exists()) {
//			dirFile.setWritable(true, false); //Linux权限设置
                dirFile.mkdirs();
            }
            //检查扩展名
            String fileExt = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toLowerCase();
            //新文件名
            String newFileName = UUID.randomUUID().toString() + "." + fileExt;
            String filePath = session.getServletContext().getRealPath("/upload/app/")+newFileName;
            try
            {
                file.transferTo(new File(filePath));
            }
            catch(Exception e)
            {
                e.printStackTrace();
//			return returnCommand("上传失败!");
            }
            System.out.println(savePath + "/" + newFileName);

            appVersion.setAttached(filePath);
            appVersion.setPosttime(new Date());
            appVersionService.save(appVersion);

            mv.setViewName("redirect:/appManagerList");
//            mv.setViewName("redirect:/firePatrolUserList?method=addSuccess");
        }
        //跳转到添加页面
        else
        {
            mv.setViewName("manager/system/appVersion/appVersion-add");
        }
        return mv;
    }
    /**
     * APP列表
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("appVersion_delete")
    public ModelAndView delete(String ids,HttpSession session)
    {
        ModelAndView mv = new ModelAndView();
        if(ids==null){
            mv.setViewName("redirect:/appManagerList");
            mv.addObject("msg","请勾选信息");
            return mv;
        }
        String[] idArr = ids.split(",");
        for(int i = 0;i < idArr.length;i++){
            int id = Integer.parseInt(idArr[i]);
            String s = String.valueOf(id);
            AppVersion byId = appVersionService.getById(s);
            byId.setIsDel(1);
            appVersionService.update(byId);
        }
        mv.setViewName("redirect:/appManagerList");
        return mv;
    }


}
