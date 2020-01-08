package com.cigit.security.distributed.uaa.dao;

import com.cigit.security.distributed.uaa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @version 1.0
 **/
@Repository
public interface UserDao extends JpaRepository<User,String> {

    User findByUsername(String userName);

}
