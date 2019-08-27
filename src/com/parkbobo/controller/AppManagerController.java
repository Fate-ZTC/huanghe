package com.parkbobo.controller;

import com.mobile.model.AppVersion;
import com.mobile.service.AppVersionService;
import com.opensymphony.xwork2.ActionContext;
import com.parkbobo.model.FirePatrolUser;
import com.system.utils.PageBean;
import com.system.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
    public ModelAndView list(AppVersion appVersion, Integer page, Integer pageSize) throws UnsupportedEncodingException
    {

//        if(model != null && model.getMemo() != null && !"".equals(model.getMemo())){
//            hql += "and memo like '%" + model.getMemo() + "%' ";
//        }

//        PageBean<AppVersion> pageBean = this.appVersionService.getPage(hql, getPageSize(),getPage());
//        ActionContext.getContext().getValueStack().set("page", pageBean);
//        return "list";
        ModelAndView mv = new ModelAndView();
        String hql = "from AppVersion as a where a.isDel = 0 and a.type = 1 ";


        hql += "order by a.posttime desc ";


//        if(firePatrolUser != null && StringUtil.isNotEmpty(firePatrolUser.getUsername()))
//        {
//            hql+=" and f.username like '%" + firePatrolUser.getUsername() + "%'";
//        }
//        if(firePatrolUser!= null && StringUtil.isNotEmpty(firePatrolUser.getJobNum())){
//            hql +=" and f.jobNum like '%"+firePatrolUser.getJobNum()+"%'";
//        }
//        hql += " order by f.id";
        PageBean<AppVersion> appVersionPage = this.appVersionService.getByHql(hql, 1, 10);
//        PageBean<FirePatrolUser> firePatrolUserPage = this.firePatrolUserService.getUsers(hql,pageSize==null?12:pageSize, page==null?1:page);
        mv.addObject("appVersionPage", appVersionPage);
        mv.addObject("appVersion",appVersion);
        mv.setViewName("manager/system/appVersion/appVersion-list");
        return mv;
    }
}
