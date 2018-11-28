package com.colossus.auth.shiro;

import com.colossus.common.dao.AuthUserMapper;
import com.colossus.common.model.AuthPermission;
import com.colossus.common.model.AuthRole;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import com.colossus.common.utils.AppConfig;
import com.colossus.common.utils.StringUtil;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Tlsy
 * @version commerce 0.0.1
 * @date 2017/4/20  11:00
 */
public class NormalRealm extends AuthorizingRealm {


   private AuthUserMapper authUserMapper;

    public NormalRealm(){

    }

    public NormalRealm(AuthUserMapper authUserMapper){
       this.authUserMapper=authUserMapper;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        AuthUser user= (AuthUser) SecurityUtils.getSubject().getSession().getAttribute(AppConfig.USER_SESSION);

        if(user != null){
            Set<String> roles = Sets.newHashSet();
            Set<String> permissions = Sets.newHashSet();
            List<AuthRole> roleList = authUserMapper.selectAuthRoleByAuthUserId(user.getId());
            List<AuthPermission> permissionList=authUserMapper.selectAuthPermissionByAuthUserId(user.getId());
            for (AuthRole role : roleList) {
                roles.add(role.getName());
                //todo 暂时没有权限资源
//               List<Resource> res = role.getResources();
//                for (Resource resource : res) {
//                    resources.add(resource.getResourceCode());
//                }
            }
            for (AuthPermission authPermission : permissionList) {
                permissions.add(authPermission.getPermissionValue());
            }
            //将角色编码放入shiro中
            simpleAuthorizationInfo.setRoles(roles);
            //将授权编码放入shiro中
            simpleAuthorizationInfo.setStringPermissions(permissions);
        }

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomUsernamePasswordToken usernamePasswordToken=(CustomUsernamePasswordToken) authenticationToken;
        String username=usernamePasswordToken.getUsername();

        AuthUserExample example=new AuthUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<AuthUser> users=authUserMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(users)){
            throw new AuthenticationException("用户"+username+"不存在");
        }

        AuthUser user=users.get(0);
        if(user.getLocked()== 1){
            throw new AuthenticationException("用户:"+user.getUsername()+"已被禁用");
        }

        //todo 验证码
        byte[] salt = StringUtil.decodeHex(user.getPassword().substring(0,16));

        return new SimpleAuthenticationInfo(new CustomPrincipal(user),user.getPassword().substring(16), ByteSource.Util.bytes(salt),getName());
    }
}
