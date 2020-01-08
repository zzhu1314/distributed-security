package com.cigit.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  17:44
 * 配置资源服务
 *
 **/
@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "res1";
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)
                //.tokenServices(tokenService())
                .tokenStore(tokenStore)//不用远程调用验证
                .stateless(true);
    }

    /**
     * 基于令牌的权限校验
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")//令牌中scope有ROLE_ADMIN
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

   /* //资源服务令牌解析服务
    @Bean
    public ResourceServerTokenServices tokenService() {
        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");//认证服务开通了该路径
        service.setClientId("c1");
        service.setClientSecret("secret");
        return service;
    }*/


}
