package com.colossus.shiro.config;

import com.colossus.shiro.*;
import com.google.common.collect.Lists;
import com.tembin.member.client.api.AuthTokenApi;
import com.tembin.member.client.api.UserApi;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.session.J2ESessionStore;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroCasConfig {


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }
    @Bean
    public List<AuthorizingRealm> normalRealm() {
        List<AuthorizingRealm> result= Lists.newArrayList();
        Pac4jRealm realm= new Pac4jRealm();
        realm.setPrincipalNameAttribute("casPac4jPrincipal");
//        realm.setCachingEnabled(true);
//        realm.setAuthorizationCachingEnabled(true);
//        realm.setAuthenticationCachingEnabled(true);
        //realm.setAuthenticationTokenClass(CustomCasToken.class);
        result.add(realm);
        return result;
    }


    @Bean
    @ConditionalOnMissingBean
    public CustomFormExtractor customFormExtractor(CasConfiguration casConfiguration,
                                                   AuthTokenApi authTokenApi,
                                                   UserApi userApi){
        CustomFormExtractor customFormExtractor=new CustomFormExtractor("phoneNumber","passwd","casRestClient");
        customFormExtractor.setConfiguration(casConfiguration);
        customFormExtractor.setAuthTokenApi(authTokenApi);
        customFormExtractor.setUserApi(userApi);
        return customFormExtractor;
    }

    @Bean
    public CasConfiguration casConfiguration( @Value("${spring.cas.prefix_url}")String prefixUrl,
                                              @Value("${spring.cas.login_url}")String casLoginUrl,
                                              RedisCache cache,
                                              DefaultWebSessionManager sessionManager){
        CasConfiguration casConfiguration = new CasConfiguration(casLoginUrl);
        casConfiguration.setProtocol(CasProtocol.CAS30);
        casConfiguration.setPrefixUrl(prefixUrl);
        casConfiguration.setLogoutHandler(defaultCasLogoutHandler(cache,sessionManager));
        return casConfiguration;
    }
    /**
     * 通过rest接口可以获取tgt，获取service ticket，甚至可以获取CasProfile
     * @return
     */

    @Bean
    public CustomRestClient casRestFormClient(CasConfiguration casConfiguration, CustomFormExtractor customFormExtractor) {

        CustomRestClient casRestFormClient = new CustomRestClient();
        casRestFormClient.setConfiguration(casConfiguration);
        casRestFormClient.setName("casRestClient");
        casRestFormClient.setCredentialsExtractor(customFormExtractor);
        return casRestFormClient;
    }



    @Bean
    public Clients clients(CustomRestClient customRestClient, CustomCasClient customCasClient) {
        //可以设置默认client
        Clients clients = new Clients();
        //支持的client全部设置进去
        clients.setClients(customCasClient,customRestClient);
        return clients;
    }



    @Bean
    public Config casConfig(Clients clients) {
        Config config = new Config();
        config.setClients(clients);
        config.setSessionStore(new J2ESessionStore());

        return config;
    }

    /**
     * 可rememberMe
     * @param callbackUrl
     * @return
     */
    @Bean
    public CustomCasClient casClient(CasConfiguration casConfiguration,
                                     @Value("${spring.cas.callback_url}")String callbackUrl) {

        CustomCasClient casClient = new CustomCasClient();
        casClient.setConfiguration(casConfiguration);
        casClient.setCallbackUrl(callbackUrl);
        casClient.setName("casClient");
        return casClient;
    }

    @Bean
    public CustomLogoutHandler defaultCasLogoutHandler(RedisCache cache, DefaultWebSessionManager sessionManager){
        CustomLogoutHandler defaultCasLogoutHandler=new CustomLogoutHandler();
        defaultCasLogoutHandler.setDestroySession(true);
        defaultCasLogoutHandler.setStore(customCacheStore(cache));
        defaultCasLogoutHandler.setSessionManager(sessionManager);
        return defaultCasLogoutHandler;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/callback", "callback");
        definition.addPathDefinition("/logout", "logout");
        //definition.addPathDefinition("/api/auth/**","auth");
        definition.addPathDefinition("/api/v1/message/getCategoryIcon","anon");
        definition.addPathDefinition("/api/v1/auth/renewToken","anon");
        definition.addPathDefinition("/api/v1/user/sendCaptcha","anon");
        definition.addPathDefinition("/api/v1/user/verifyCaptchaAndGetInvitations","anon");
        definition.addPathDefinition("/api/v1/user/register","anon");
        definition.addPathDefinition("/api/v1/user/resetPwd","anon");
        definition.addPathDefinition("/api/v1/user/sendResetPwdCaptcha","anon");
        definition.addPathDefinition("/api/v1/user/login", "anon");

        definition.addPathDefinition("/api/v1/user/saveUserPosition","anon");
        definition.addPathDefinition("/api/v1/android/**","anon");
        definition.addPathDefinition("/api/v1/app/picture/**","anon");
        definition.addPathDefinition("/api/v2/ios/check","anon");
        definition.addPathDefinition("/api/v2/review/helper/**","anon");
        definition.addPathDefinition("/api/v2/mailgun/**","anon");
        definition.addPathDefinition("/api/v1/internal/call/**","anon");
        definition.addPathDefinition("/api/member/*","anon");
        definition.addPathDefinition("/service/**", "anon");
        definition.addPathDefinition("/alipay/**", "anon");

        definition.addPathDefinition("/error/**","anon");
        definition.addPathDefinition("/register/**","anon");
        definition.addPathDefinition("/authcode/**","anon");
        definition.addPathDefinition("/resource/**","anon");
        definition.addPathDefinition("/public/**","anon");
        definition.addPathDefinition("/kamobile/**","anon");
        definition.addPathDefinition("/tiny-app/*.html","anon");
        definition.addPathDefinition("/hook/**", "anon");
        definition.addPathDefinition("/**", "user");
        //definition.addPathDefinition("/api/v1/ebay-call-back/**", "anon");
        return definition;
    }

    /**
     * 由于cas代理了用户，所以必须通过cas进行创建对象
     *
     * @return
     */
    @Bean
    public SubjectFactory subjectFactory() {
        return new Pac4jSubjectFactory();
    }


    @Bean("shiroRedisCache")
    public RedisCache cache(@Qualifier("redisTemplate")
                                 RedisTemplate redisTemplate){
        return new RedisCache("redis_", redisTemplate,1800);
    }
    @Bean
    public CustomCacheStore customCacheStore(RedisCache cache){
        return new CustomCacheStore(cache);
    }
    /**
     * 对过滤器进行调整
     *
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition,
                                                         Config config ,
                                                         @Value("${spring.cas.default_url}")String defaultUrl) {

        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        filterFactoryBean.setSecurityManager(securityManager);

        //过滤器设置
//
//        对shiro的过滤策略进行明确
//        @return
//
        Map<String, Filter> filters = new HashMap<>();
        SecurityFilter securityFilter = new SecurityFilter();
        securityFilter.setClients("renewCasClient");
//isFullyAuthenticated
        //securityFilter.setAuthorizers("isFullyAuthenticated");

        securityFilter.setConfig(config);
        //securityFilter.setSecurityLogic(new CustomSecurityLogic());
        filters.put("auth", securityFilter);
        SecurityFilter customSecurityFilter = new SecurityFilter();
        customSecurityFilter.setClients("casClient,casRestClient");

        //securityFilter.setAuthorizers("isRemembered");
        customSecurityFilter.setSecurityLogic(new CustomSecurityLogic());
        customSecurityFilter.setConfig(config);
        filters.put("user", customSecurityFilter);
        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(config);
        callbackFilter.setDefaultUrl(defaultUrl);
        callbackFilter.setCallbackLogic(new CustomCallbackLogic<>());
        filters.put("callback", callbackFilter);
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setConfig(config);
        logoutFilter.setCentralLogout(true);
        filters.put("logout", logoutFilter);
        filterFactoryBean.setFilters(filters);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        return filterFactoryBean;
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
    @Bean("shiroRedisSessionDao")
    public RedisSessionDao redisSessionDAO(@Qualifier("redisTemplate")
                                                       RedisTemplate redisTemplate) {
        return new RedisSessionDao(redisTemplate,"redis_shiro_session_",1800);
    }

    @Bean
    public SimpleSessionFactory simpleSessionFactory(){
        return new SimpleSessionFactory();
    }

    @Bean("shiroCacheManage")
    public RedisCacheManager cacheManager(RedisCache redisCache){
        return new RedisCacheManager(redisCache);
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager SessionManager(@Qualifier("shiroCacheManage") RedisCacheManager cacheManager,@Qualifier("shiroRedisSessionDao") RedisSessionDao redisSessionDao, SimpleSessionFactory simpleSessionFactory, @Value("${server.servlet.context-path}")String contextPath) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDao);
        sessionManager.setGlobalSessionTimeout(1800*1000);
        sessionManager.setCacheManager(cacheManager);
        Cookie cookie=new SimpleCookie();
        cookie.setPath(contextPath);
        cookie.setName("auth-id");
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionFactory(simpleSessionFactory);
        return sessionManager;
    }
    @Bean
    public SecurityManager securityManager(DefaultWebSessionManager sessionManager, @Qualifier("shiroCacheManage")RedisCacheManager cacheManager, List<AuthorizingRealm> realms) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        Cookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        //设置为7天
        cookie.setMaxAge(60 * 60 * 24 *7);
        cookieRememberMeManager.setCookie(cookie);
        securityManager.setRememberMeManager(cookieRememberMeManager);
        // 设置realm.
        securityManager.setRealms(Lists.newArrayList(realms));
        securityManager.setSubjectFactory(subjectFactory());
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

}
