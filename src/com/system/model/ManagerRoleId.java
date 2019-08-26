package com.system.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @ClassName ManagerRoleId
 * @Description TODO
 * @Author Administrator
 * @Date 2019/8/20 9:20
 * @Version 1.0
 **/
@Embeddable
public class ManagerRoleId implements java.io.Serializable{
    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 1830011059263731127L;
    private int managerId;
    private int roleId;


    // Constructors

    /** default constructor */
    public ManagerRoleId() {
    }


    /** full constructor */
    public ManagerRoleId(int managerId, int roleId) {
        this.managerId = managerId;
        this.roleId = roleId;
    }


    // Property accessors

    @Column(name="user_id", nullable=false)
    public int getManagerId() {
        return this.managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Column(name="role_id", nullable=false)
    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }




    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( (other == null ) ) return false;
        if ( !(other instanceof ManagerRoleId) ) return false;
        ManagerRoleId castOther = ( ManagerRoleId ) other;

        return (this.getRoleId()==castOther.getRoleId())
                && (this.getManagerId()==castOther.getManagerId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getRoleId();
        result = 37 * result + (int) this.getManagerId();
        return result;
    }
}
