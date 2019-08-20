package com.system.model;

import javax.persistence.*;

/**
 * @ClassName ManagerRole
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/19 17:49
 * @Version 1.0
 **/
@Entity
@Table(name = "manager_role")
public class ManagerRole implements java.io.Serializable{
    private static final long serialVersionUID = -7180466272474998060L;
    private ManagerRoleId id;
    private Manager manager;
    private Role role;

    // Property accessors
    @EmbeddedId

    @AttributeOverrides( {
            @AttributeOverride(name="managerId", column=@Column(name="user_id", nullable=false) ),
            @AttributeOverride(name="roleId", column=@Column(name="role_id", nullable=false) ) } )

    public ManagerRoleId getId() {
        return this.id;
    }

    public void setId(ManagerRoleId id) {
        this.id = id;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
