package com.system.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.system.model.ManagerRole;
import com.system.service.ManagerRoleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.PageBean;
import com.system.model.Department;
import com.system.model.Manager;
import com.system.model.Role;
import com.system.service.DepartmentService;
import com.system.service.ManagerService;
import com.system.service.RoleService;
import com.system.utils.StringUtil;

/**
 * 系统用户管理
 * @author LH
 * @since 1.0
 */
@Controller
@Scope("session")
public class ManagerController {
    private static final long serialVersionUID = -210001639179001824L;
    @Resource
    private ManagerService managerService;
    @Resource
    private ManagerRoleService managerRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private DepartmentService departmentService;
    private PageBean<Manager> managerPage;
    private Manager manager;
    private Integer id;
    private List<Role> roleList;
    private List<Department> departmentList;
    private String regionJson;

    /**
     * 系统用户列表
     * @return
     */
    @RequestMapping("manager_list")
    public ModelAndView list(Manager manager,Integer pageSize,Integer page){
        ModelAndView mv = new ModelAndView();
        String hql = "from Manager as u where 1 = 1 ";
        if(manager != null && StringUtil.isNotEmpty(manager.getUsername())) {
            hql += " and u.username like '%" + manager.getUsername() + "%'";
        }
        if(manager != null && StringUtil.isNotEmpty(manager.getRealname())){
            hql += " and u.realname like '%" + manager.getRealname() + "%'";
        }
        hql += " order by u.registerTime desc";
        managerPage = this.managerService.loadPage(hql,pageSize==null?12:pageSize, page==null?1:page);
        mv.addObject("managerPage", managerPage);
        mv.setViewName("manager/system/manager/manager-list");
        return mv;
    }
    /**
     * 编辑系统用户
     * @return
     */
    @RequestMapping("manager_edit")
    public ModelAndView edit(String method,HttpSession session,Integer id,Manager manager,String enablRegionIds,String[] roles){
        //编辑
        ModelAndView mv = new ModelAndView();
        if(StringUtil.isNotEmpty(method) && method.equals("edit"))
        {
            Manager user = (Manager) session.getAttribute("loginUser");
            manager.setRegisterTime(new Date());
            managerService.update(manager, enablRegionIds, user, roles);
            mv.setViewName("redirect:/manager_list?method=editSuccess");
        }
        //跳转到编辑页面
        else
        {
            manager = managerService.getById(id);
            roleList = roleService.getByHql("from Role as r where r.roleType = 1 order by r.createTime desc");
            departmentList = departmentService.getByHql("from Department as d order by d.createTime desc");
            mv.addObject("manager",manager);
            mv.addObject("roleList",roleList);
            mv.addObject("departmentList",departmentList);
            mv.setViewName("manager/system/manager/manager-edit");
        }
        return mv;
    }
    /**
     * 添加系统用户
     * @return
     */
    @RequestMapping("manager_add")
    public ModelAndView add(String method,HttpSession session,Manager manager,String enablRegionIds,String tex,String[] roles){
        //添加
        ModelAndView mv = new ModelAndView();
        if(StringUtil.isNotEmpty(method) && method.equals("add"))
        {
            Manager user = (Manager) session.getAttribute("loginUser");
            manager.setRegisterTime(new Date());
            managerService.add(manager, enablRegionIds, user, roles);

            //得到当前添加的用户Id
//			int userId1=manager.getUserId();
//			for(int i = 0 ; i < roles.length ; i++){
//				Manager manager1=new Manager();
//				manager1.setUserId(userId1);
//				Role role1=new Role();
//				role1.setRoleId(Integer.valueOf(roles[i]));
//				ManagerRole managerRole=new ManagerRole();
//				managerRole.setManager(manager1);
//				managerRole.setRole(role1);
//				managerRoleService.add(managerRole);
//			}
            mv.setViewName("redirect:/manager_list?method=addSuccess");
        }
        //跳转到添加页面
        else
        {
            roleList = roleService.getByHql("from Role as r where r.roleType = 1 order by r.createTime desc");
            departmentList = departmentService.getByHql("from Department as d order by d.createTime desc");
            mv.addObject("roleList",roleList);
            mv.addObject("departmentList",departmentList);
            mv.setViewName("manager/system/manager/manager-add");
        }
        return mv;
    }
    /**
     * 删除系统用户
     * @return
     */
    @RequestMapping("manager_delete")
    public ModelAndView delete(String ids,HttpSession session)
    {
        ModelAndView mv = new ModelAndView();
        Manager user = (Manager) session.getAttribute("loginUser");
        this.managerService.bulkDelete(ids, user);
        mv.setViewName("redirect:/manager_list?method=deleteSuccess");
        return mv;
    }
    /**
     * 重置系统用户密码
     * @return
     */
    @RequestMapping("manager_resetPassword")
    public ModelAndView resetPassword(HttpSession session){
        ModelAndView mv = new ModelAndView();
        Manager user = (Manager) session.getAttribute("loginUser");
        this.managerService.resetPassword(id, user);
        mv.setViewName("redirect:/manager_list?method=restSuccess");
        return mv;
    }


    public List<Role> getRoleList() {
        return roleList;
    }
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
    public List<Department> getDepartmentList() {
        return departmentList;
    }
    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegionJson() {
        return regionJson;
    }

    public void setRegionJson(String regionJson) {
        this.regionJson = regionJson;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public PageBean<Manager> getManagerPage() {
        return managerPage;
    }

    public void setManagerPage(PageBean<Manager> managerPage) {
        this.managerPage = managerPage;
    }

}
