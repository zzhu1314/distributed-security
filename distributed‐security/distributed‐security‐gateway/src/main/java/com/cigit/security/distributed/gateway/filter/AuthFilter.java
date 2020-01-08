package com.cigit.security.distributed.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.cigit.security.distributed.gateway.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/8  14:20
 **/
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        /**
         * 1.获取令牌
         */
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof OAuth2Authentication)){
            return null;
        }
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
        Authentication userAuthentication = oauth2Authentication.getUserAuthentication();
        String principal = oauth2Authentication.getName();
        /**
         * 2.组装成明文token，转发给微服务
         */
        List<String> authorities = new ArrayList();
        userAuthentication.getAuthorities().stream().forEach(s->authorities.add(((GrantedAuthority) s).getAuthority()));
        OAuth2Request oAuth2Request = oauth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String,Object> jsonToken = new HashMap<>(requestParameters);
        if(userAuthentication != null){
            jsonToken.put("principal",principal);
            jsonToken.put("authorities",authorities);
        }
        ctx.addZuulRequestHeader("json‐token",
                EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        return null;
    }
}
