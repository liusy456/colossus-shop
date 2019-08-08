package com.colossus.shiro;


import com.colossus.common.utils.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisSessionDao extends AbstractSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);
    private RedisTemplate redisTemplate;
    private String keyPrefix = "redis_shiro_session_";
    private int expireTime;

    public RedisSessionDao(RedisTemplate redisTemplate, String prefix, int expireTime) {
        this.redisTemplate=redisTemplate;
        this.expireTime=expireTime;
        this.keyPrefix=prefix;
    }

    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            String key = (String)session.getId();
            session.setTimeout((expireTime * 1000));
            String value = SerializableUtil.serialize(session);
            this.redisTemplate.opsForValue().set(keyPrefix+key, value);
            redisTemplate.expire(keyPrefix+key,expireTime, TimeUnit.SECONDS);
        } else {
            logger.error("session or session id is null");
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            this.redisTemplate.delete(keyPrefix+session.getId());
        } else {
            logger.error("session or session id is null");
        }
    }

    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        Set<String> keys = this.redisTemplate.keys(this.keyPrefix+"*"); //有*才能匹配出该前缀的值
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session s = SerializableUtil.deserialize((String)this.redisTemplate.opsForValue().get(key));
                sessions.add(s);
            }
        }

        return sessions;
    }

    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        } else {
            return SerializableUtil.deserialize((String)this.redisTemplate.opsForValue().get(keyPrefix+sessionId));
        }
    }
}
