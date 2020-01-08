package com.cigit.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Administrator
 * @version 1.0
 * 基于用户访问的认证和权限的校验
 *
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)//开启@Secured注解使用,@PreAuthorize和@PostAuthorize注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //配置安全拦截

    /**
     * 建议使用基于资源授权不要使用角色授权，要授权的细节放前面，否则不会生效
     * 通常使用基于方法授权
     * 基于用户访问的认证和权限的校验
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/r/**").authenticated()//匹配/r/**的必须要安全认证
                .anyRequest().permitAll()//任何请求，放行
                .and()
                .formLogin()//允许表单提交
                .successForwardUrl("/login-success");//成功跳转路径


    }
}
