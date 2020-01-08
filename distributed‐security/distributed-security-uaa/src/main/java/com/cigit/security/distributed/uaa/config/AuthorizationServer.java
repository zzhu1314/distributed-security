package com.cigit.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  16:46
 * 授权服务配置
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /*//配置客户端信息（从内存中读取）
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.inMemory()// 使用in‐memory存储
                .withClient("c1")// client_id
                .secret(new BCryptPasswordEncoder().encode("secret"))//秘钥
                .resourceIds("res1")
                .authorizedGrantTypes("authorization_code",
                        "password", "client_credentials", "implicit", "refresh_token")// 该client允许的授权类型 authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                .autoApprove(false)
//加上验证回调地址
                .redirectUris("http://www.baidu.com");
    }*/

    /**
     * 数据库读取客户端信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 从数据库读取客户端信息
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    //令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        service.setSupportRefreshToken(true);//支持刷新令牌
        service.setTokenStore(tokenStore);//绑定tokenStore
        //增强令牌
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

   //配置令牌（token）的访问端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)//认证管理器
                .authorizationCodeServices(authorizationCodeServices)//授权码服务
                .tokenServices(tokenService())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);//允许post提交
    }

    /**
     * 3.配置令牌（token）的访问端点
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
    }

  /*  @Bean
    public AuthorizationCodeServices authorizationCodeServices() { //设置授权码模式的授权码如何存取，暂时采用内存方式
        return new InMemoryAuthorizationCodeServices();
    }*/

    /*
     *令牌端点(Token Endpoint)的安全约束
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")//oauth/token_key 公开
                .checkTokenAccess("permitAll()")//oauth/check_token这个endpoint完全公开
                .allowFormAuthenticationForClients();//允许表单认证(申请令牌)
    }

}
