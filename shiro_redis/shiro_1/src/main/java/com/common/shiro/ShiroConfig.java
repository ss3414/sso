package com.common.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /* MD5加密，加密1次，使用Hex存储 */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /* 自定义Realm */
    @Bean
    public RBACRealm RBACRealm() {
        RBACRealm myRealm = new RBACRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myRealm;
    }

    /*
     * SessionManager
     * ①redisOpen必须写在sessionManager方法参数内，否则无法获取默认值
     * ②RedisShiroSessionDao不能使用new配置，否则RedisShiroSessionDao中的RedisTemplate注入为null
     * */
    @Bean
    public SessionManager sessionManager(@Value("${redis.open}") Boolean redisOpen, RedisShiroSessionDao redisShiroSessionDao) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        if (redisOpen) {
            defaultWebSessionManager.setSessionDAO(redisShiroSessionDao); /* 启用Redis管理Shiro Session */
        }
        return defaultWebSessionManager;
    }

    /* SecurityManager */
    @Bean
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(RBACRealm());
        defaultWebSecurityManager.setSessionManager(sessionManager);
        return defaultWebSecurityManager;
    }

    /* ShiroFilter */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /* 登录/登录成功/未授权URL */
        shiroFilterFactoryBean.setLoginUrl("/login/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login/noPermission"); /* 生效 */

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/doLogout", "logout");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /* 开启Shiro AOP注解 */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /* Shiro SpringMVC配置需要 */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*
     * ①Shiro SpringMVC配置（开启Shiro注解）
     * ②依赖于LifecycleBeanPostProcessor
     * */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
