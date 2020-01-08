package com.cigit.security.distributed.uaa.service;

import com.alibaba.fastjson.JSON;
import com.cigit.security.distributed.uaa.dao.UserDao;
import com.cigit.security.distributed.uaa.model.Role;
import com.cigit.security.distributed.uaa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/6  17:48
 **/
@Service
public class SpringDataUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("账号:" + userName);
        User userDto = userDao.findByUsername(userName);
        if (userDto == null) {
            return null;
        }
        Set<Role> roleList = userDto.getRoleList();
        List<String> list = new ArrayList<>();
        roleList.stream().forEach(r-> r.getPermissionList().stream().forEach(p-> list.add(p.getCode())));
        String[] array = new String[list.size()];
        list.toArray(array);
        String s = JSON.toJSONString(userDto);
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder().username(s).password(userDto.getPassword()).authorities(array).build();
        return userDetails;
    }
}
