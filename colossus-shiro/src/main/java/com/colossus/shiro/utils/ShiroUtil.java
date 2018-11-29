package com.colossus.shiro.utils;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.pac4j.core.profile.CommonProfile;

public class ShiroUtil {

    /**
     *  获取当前机构Id
     * @return
     */
    public static String getCurrentOrgId(){
        return getProfileAttribute("org_id");
    }

    /**
     * 获取当前用户Id
     * @return
     */
    public static String getCurrentUserId(){
       return getProfileAttribute("id");
    }

    /**
     *  从CAS的Principal里面获取参数值。如果没有成功获取到则返回null
     * @param attrName
     * @return
     */
    public static String getProfileAttribute(String attrName){
        try {
            Subject subject=SecurityUtils.getSubject();
            if(subject!=null){
                Pac4jPrincipal pac4jPrincipal=(Pac4jPrincipal) subject.getPrincipal();
                if(pac4jPrincipal!=null){
                    CommonProfile profile=pac4jPrincipal.getProfile();
                    if(profile!=null){
                        return (String)profile.getAttribute(attrName);
                    }
                }
            }
            return null;
        }catch (UnavailableSecurityManagerException e){
            return null;
        }
    }
}
