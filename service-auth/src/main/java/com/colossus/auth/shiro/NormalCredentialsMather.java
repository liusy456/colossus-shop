package com.colossus.auth.shiro;


import com.colossus.common.utils.DateUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Tlsy
 * @version commerce 0.0.1
 * @date 2017/4/19  18:40
 */
public class NormalCredentialsMather extends CustomCredentialsMatcher {


    /**
     * 多次登录失败后需要等待的时长(毫秒)
     * 现在为3分钟
     */
    private static final long WAITING_TIME_AFTER_LOGIN_FAILED = 3* DateUtil.MILLIS_PER_MINUTE;
    /**
     * 最多可以尝试登录的次数
     */
    private static final int MAX_RETRY_TIMES = 5;

    // 集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    private Cache<String, AtomicInteger> retryCountCache;
    private Cache<String, AtomicLong> accountLockTimeCache;

    public NormalCredentialsMather(CacheManager cacheManager, String hashAlgorithmName, int hashIterations){
        setHashAlgorithmName(hashAlgorithmName);
        setHashIterations(hashIterations);
        retryCountCache= cacheManager.getCache("passwordRetryCache");
        accountLockTimeCache=cacheManager.getCache("passwordRetryCache");
    }


    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        CustomUsernamePasswordToken usernamePasswordToken=(CustomUsernamePasswordToken) authenticationToken;
        String username=usernamePasswordToken.getUsername();

        Object tokenHashedCredentials = this.hashProvidedCredentials(authenticationToken, authenticationInfo);
        Object accountCredentials = this.getCredentials(authenticationInfo);
       // return this.equals(tokenHashedCredentials, accountCredentials);

        boolean pass=this.equals(tokenHashedCredentials, accountCredentials);
        if(pass){
            retryCountCache.remove(username);
            //saveUserInfo(username);
            return pass;
        }

        return false;
    }

    /**
     * 检查是否超过了登录次数
     * @param phone
     */
    private void checkIsLock(String phone){
        AtomicLong lastLockTime = accountLockTimeCache.get(phone+"_time");
        if(lastLockTime==null){
            return;
        }
        if(System.currentTimeMillis()-lastLockTime.get()>WAITING_TIME_AFTER_LOGIN_FAILED){
            accountLockTimeCache.remove(phone+"_time");
        }else {
            throw new ExcessiveAttemptsException(sendErrorMessage(MAX_RETRY_TIMES));
        }
    }

    /**
     * 登录错误后更新登录错误次数
     * @param phone
     */
    private void updateRetryCount(String phone){
        AtomicInteger retryCount = retryCountCache.get(phone);
        if(retryCount == null){
            retryCount = new AtomicInteger(1);
            retryCountCache.put(phone, retryCount);
        }else if(retryCount.incrementAndGet() == MAX_RETRY_TIMES){
            retryCountCache.remove(phone);
            accountLockTimeCache.put(phone+"_time", new AtomicLong(System.currentTimeMillis()));
            throw new ExcessiveAttemptsException(sendErrorMessage(retryCount.get()));
        }
        throw new IncorrectCredentialsException(sendErrorMessage(retryCount.get()));

    }

    /**
     * 根据密码错误次数创建不同的提示消息
     * @param retryTimes
     * @return
     */
    private String sendErrorMessage(int retryTimes){
        if(retryTimes >= MAX_RETRY_TIMES){
            return "你已经连续输入"+MAX_RETRY_TIMES+"次错误密码，请"+WAITING_TIME_AFTER_LOGIN_FAILED/60000+"分钟后重试";
        }else{
            return "用户名或密码错误,你还有"+(MAX_RETRY_TIMES-retryTimes)+"次尝试机会，超出后账号将被锁定"+WAITING_TIME_AFTER_LOGIN_FAILED/3600000+"小时";
        }
    }
}
