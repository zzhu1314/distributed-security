package com.cigit.security.distributed.uaa.dao;

import com.cigit.security.distributed.uaa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  10:26
 **/
@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
}
