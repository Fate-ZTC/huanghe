package com.system.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_manager"
)
@SequenceGenerator(name="generator",sequenceName="lq_manager_user_id_seq",allocationSize=1)
public class Manager  implements java.io.Serializable {
     /**
	 * 
	 */
	private static final long serialVersionUID = 4133811498053033027L;
	private Integer userId;
    private Department department;
    private String email;
    private String username;
    private String nickname;
    private String password;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Integer loginCount;
    private String resetKey;
    private Date registerTime;
    private String registerIp;
    private Integer activation;
    private String activationCode;
    private String realname;
    private String qq;
    private String phone;
    private String mobile;
    private String userImg;
    private Integer isAuth;
    private Integer status;
    private String memo;
    private Set<ManagerRole> managerRoles = new HashSet<ManagerRole>(0);
    private Set<ManagerResources> managerResourceses = new HashSet<ManagerResources>(0);
    
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")
    @Column(name="user_id", unique=true, nullable=false)

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	@ManyToOne(fetch=FetchType.EAGER)
        @JoinColumn(name="departmentid")
    public Department getDepartment() {
        return this.department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    @Column(name="email", length=50)

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name="username", unique=true, nullable=false, length=50)

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Column(name="nickname", length=50)

    public String getNickname() {
        return this.nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    @Column(name="password", nullable=false, length=100)

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Column(name="last_login_time", length=29)

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }
    
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    @Column(name="last_login_ip", length=50)

    public String getLastLoginIp() {
        return this.lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    
    @Column(name="login_count", nullable=false)

    public Integer getLoginCount() {
        return this.loginCount;
    }
    
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }
    
    @Column(name="reset_key", length=32)

    public String getResetKey() {
        return this.resetKey;
    }
    
    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }
    
    @Column(name="register_time", nullable=false, length=29)

    public Date getRegisterTime() {
        return this.registerTime;
    }
    
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
    
    @Column(name="register_ip", length=50)

    public String getRegisterIp() {
        return this.registerIp;
    }
    
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }
    
    @Column(name="activation", nullable=false)

    public Integer getActivation() {
        return this.activation;
    }
    
    public void setActivation(Integer activation) {
        this.activation = activation;
    }
    
    @Column(name="activation_code", length=32)

    public String getActivationCode() {
        return this.activationCode;
    }
    
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    
    @Column(name="realname", length=50)

    public String getRealname() {
        return this.realname;
    }
    
    public void setRealname(String realname) {
        this.realname = realname;
    }
    
    @Column(name="qq", length=20)

    public String getQq() {
        return this.qq;
    }
    
    public void setQq(String qq) {
        this.qq = qq;
    }
    
    @Column(name="phone", length=20)

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Column(name="mobile", length=20)

    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    @Column(name="user_img")

    public String getUserImg() {
        return this.userImg;
    }
    
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
    
    @Column(name="is_auth", nullable=false)

    public Integer getIsAuth() {
        return this.isAuth;
    }
    
    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }
    
    @Column(name="status", nullable=false)

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="manager")
	public Set<ManagerResources> getManagerResourceses() {
		return managerResourceses;
	}

	public void setManagerResourceses(Set<ManagerResources> managerResourceses) {
		this.managerResourceses = managerResourceses;
	}

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="manager")
    public Set<ManagerRole> getManagerRoles() {
        return managerRoles;
    }

    public void setManagerRoles(Set<ManagerRole> managerRoles) {
        this.managerRoles = managerRoles;
    }
}