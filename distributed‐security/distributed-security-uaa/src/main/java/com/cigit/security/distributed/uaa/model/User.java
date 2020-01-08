package com.cigit.security.distributed.uaa.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Administrator
 * @version 1.0
 * 多对多查询，若相互包含，会造成栈溢出
 **/
@Entity
@Table(name="t_user")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String mobile;

    /**
     * 用户拥有所有角色
     */
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role",joinColumns = {@JoinColumn(name="user_id")}
    ,inverseJoinColumns = {@JoinColumn(name="role_id")})
    private Set<Role> roleList;


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
