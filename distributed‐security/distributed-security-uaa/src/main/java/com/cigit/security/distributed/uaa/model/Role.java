package com.cigit.security.distributed.uaa.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  10:17
 **/
@Entity
@Table(name="t_role")
@Getter
@Setter
public class Role {
    @Id
    private String id;
    private String roleName;
    private String description;
    private Date createTime;
    private Date updateTime;
    private String status;

    /**
     * 角色拥有所有权限
     */
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name="t_role_permission",joinColumns = {@JoinColumn(name="role_id")}
            ,inverseJoinColumns = {@JoinColumn(name="permission_id")})
    private Set<Permission> permissionList;



    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                ", permissionList=" + permissionList +
                '}';
    }
}
