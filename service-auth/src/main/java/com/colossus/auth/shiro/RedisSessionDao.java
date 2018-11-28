package com.colossus.auth.shiro;

import com.colossus.auth.utils.SerializableUtil;
import com.colossus.redis.service.RedisService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDao extends AbstractSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);
    private RedisService redisService;
    private String keyPrefix = "shiro_redis_session:";
    private int expireTime;

    public RedisSessionDao(RedisService redisService,String prefix,int expireTime) {
        this.redisService=redisService;
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
            this.redisService.hset(keyPrefix,key, value);
            redisService.expire(keyPrefix,expireTime);
        } else {
            logger.error("session or session id is null");
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            this.redisService.hdel(keyPrefix,(String)session.getId());
        } else {
            logger.error("session or session id is null");
        }
    }

    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        Set<String> keys = this.redisService.keys(this.keyPrefix);
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session s = SerializableUtil.deserialize(this.redisService.hget(keyPrefix,key));
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
            return SerializableUtil.deserialize(this.redisService.hget(keyPrefix,(String)sessionId));
        }
    }
}
