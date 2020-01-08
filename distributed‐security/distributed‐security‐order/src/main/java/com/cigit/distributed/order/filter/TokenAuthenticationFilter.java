package com.cigit.distributed.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cigit.distributed.order.dto.UserDTO;
import com.cigit.distributed.order.util.EncryptUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/8  14:57
 **/
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("json‐token");
        if (token != null) {
            //1.解析token
            String json = EncryptUtil.decodeUTF8StringBase64(token);
            JSONObject userJson = JSON.parseObject(json);
            UserDTO user = new UserDTO();
            user.setUsername(userJson.getString("principal"));
            JSONArray authoritiesArray = userJson.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new
                    String[authoritiesArray.size()]);
            //2.新建并填充authentication
            UsernamePasswordAuthenticationToken authentication = new
                    UsernamePasswordAuthenticationToken(
                    user, null, AuthorityUtils.createAuthorityList(authorities));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                    httpServletRequest));
            //3.将authentication保存进安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
