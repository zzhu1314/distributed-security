package com.cigit.security.distributed.uaa.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  10:28
 **/
@Entity
@Table(name="t_permission")
@Getter
@Setter
public class Permission {
    @Id
    private String id;
    private String code;
    private String description;
    private String url;


    @Override
    public String toString() {
        return "Permission{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
