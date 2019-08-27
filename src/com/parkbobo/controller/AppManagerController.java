package com.parkbobo.controller;

import com.parkbobo.model.FirePatrolUser;
import com.parkbobo.utils.PageBean;
import com.system.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;

/**
 * APP管理
 * @author zj
 *@version 1.0
 */
@Controller
public class AppManagerController {
    /**
     * 巡查人员
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("appManagerList")
    public ModelAndView list(FirePatrolUser firePatrolUser, Integer page, Integer pageSize) throws UnsupportedEncodingException
    {
        ModelAndView mv = new ModelAndView();

        String hql = "from  FirePatrolUser f where isDel = 0";

        if(firePatrolUser != null && StringUtil.isNotEmpty(firePatrolUser.getUsername()))
        {
            hql+=" and f.username like '%" + firePatrolUser.getUsername() + "%'";
        }
        if(firePatrolUser!= null && StringUtil.isNotEmpty(firePatrolUser.getJobNum())){
            hql +=" and f.jobNum like '%"+firePatrolUser.getJobNum()+"%'";
        }
        hql += " order by f.id";
//        PageBean<FirePatrolUser> firePatrolUserPage = this.firePatrolUserService.getUsers(hql,pageSize==null?12:pageSize, page==null?1:page);
//        mv.addObject("firePatrolUserPage", firePatrolUserPage);
//        mv.addObject("firePatrolUser",firePatrolUser);
//        mv.setViewName("manager/system/firePatrol/fireUser-list");
        return mv;
    }
}
