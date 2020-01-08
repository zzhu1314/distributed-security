package com.cigit.distributed.order.controller;

import com.alibaba.fastjson.JSON;
import com.cigit.distributed.order.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  17:43
 **/
@RestController
public class OrderController {
    @PreAuthorize("hasAuthority('p1')")
    @GetMapping(value = "/r1")
    public String r1() {
        String str = (String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = JSON.parseObject(str, UserDTO.class);
        return userDTO.getUsername()+ "访问资源1";
    }

    @PreAuthorize("hasAuthority('p2')")
    @GetMapping(value = "/r2")
    public String r2() {//通过Spring Security API获取当前登录用户
        UserDTO user =
                (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername() + "访问资源2";
    }
}
