package com.colossus.auth.shiro;

import com.colossus.common.dao.AuthFilterMapper;
import com.colossus.common.dao.AuthUserMapper;
import com.colossus.common.model.AuthFilter;
import com.colossus.common.model.AuthFilterExample;
import com.colossus.redis.service.RedisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager,AuthFilterMapper authFilterMapper) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        // 权限控制map.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 从数据库获取
        AuthFilterExample example=new AuthFilterExample();
        example.createCriteria().andIdIsNotNull();
        List<AuthFilter> list = authFilterMapper.selectByExample(example);

        for (AuthFilter authFilter : list) {
            filterChainDefinitionMap.put(authFilter.getUrl(),
                    authFilter.getFilter());
        }
        Map<String,Filter> filters=Maps.newHashMap();
        filters.put("authc",new CustomFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        System.out.println("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(DefaultWebSessionManager sessionManager,CacheManager cacheManager,List<AuthorizingRealm> realms) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealms(Lists.newArrayList(realms));
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        ModularRealmAuthenticator authenticator=new ModularRealmAuthenticator();
        authenticator.setRealms(Lists.newArrayList(realms));
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);
        //todo rememberManager
        return securityManager;
    }

    @Bean
    public CacheManager cacheManager(RedisCache redisCache){
        return new RedisCacheManager(redisCache);
    }

    @Bean
    public RedisCache cache( RedisService redisService){
        return new RedisCache("shiro_session_",redisService);
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public List<AuthorizingRealm> normalRealm(CacheManager cacheManager, AuthUserMapper authUserMapper) {
        List<AuthorizingRealm> result= Lists.newArrayList();
        NormalRealm normalRealm=new NormalRealm(authUserMapper);
        normalRealm.setCacheManager(cacheManager);
        normalRealm.setCredentialsMatcher(new NormalCredentialsMather(cacheManager,"SHA-1",1024));
        normalRealm.setCachingEnabled(true);
        result.add(normalRealm);
        return result;
    }

    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     */
    @Bean
    public RedisSessionDao redisSessionDAO(RedisService redisService) {
        return new RedisSessionDao(redisService,"shiro_session_",1800);
    }

    @Bean
    public SimpleSessionFactory simpleSessionFactory(){
        return new SimpleSessionFactory();
    }
    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager SessionManager(CacheManager cacheManager,RedisSessionDao redisSessionDao,SimpleSessionFactory simpleSessionFactory) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDao);
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setGlobalSessionTimeout(1800);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionFactory(simpleSessionFactory);
        return sessionManager;
    }
}
