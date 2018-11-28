package com.colossus.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.colossus.auth.service.AuthPermissionService;
import com.colossus.common.dao.AuthPermissionMapper;
import com.colossus.common.dao.AuthRolePermissionMapper;
import com.colossus.common.dao.AuthSystemMapper;
import com.colossus.common.dao.AuthUserPermissionMapper;
import com.colossus.common.model.*;
import com.colossus.common.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
@Api(value = "authPermission-service",description = "用户权限服务")
public class AuthPermissionServiceImpl extends BaseServiceImpl<AuthPermissionMapper,AuthPermission,AuthPermissionExample> implements AuthPermissionService {

    @Autowired
    private AuthSystemMapper authSystemMapper;
    @Autowired
    private AuthPermissionMapper authPermissionMapper;
    @Autowired
    private AuthUserPermissionMapper authUserPermissionMapper;
    @Autowired
    private AuthRolePermissionMapper authRolePermissionMapper;

    @Override
    public JSONArray getTreeByRoleId(String roleId) {
        // 角色已有权限
        AuthRolePermissionExample example=new AuthRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<AuthRolePermission> rolePermissions=authRolePermissionMapper.selectByExample(example);

        JSONArray systems = new JSONArray();
        // 系统
        AuthSystemExample AuthSystemExample = new AuthSystemExample();
        AuthSystemExample.createCriteria()
                .andStatusEqualTo((byte) 1);
        AuthSystemExample.setOrderByClause("orders asc");
        List<AuthSystem> authSystems = authSystemMapper.selectByExample(AuthSystemExample);
        for (AuthSystem authSystem : authSystems) {
            JSONObject node = new JSONObject();
            node.put("id", authSystem.getId());
            node.put("name", authSystem.getTitle());
            node.put("nocheck", true);
            node.put("open", true);
            systems.add(node);
        }

        if (systems.size() > 0) {
            for (Object system: systems) {
                AuthPermissionExample authPermissionExample = new AuthPermissionExample();
                authPermissionExample.createCriteria()
                        .andStatusEqualTo((byte) 1)
                        .andSystemIdEqualTo(((JSONObject) system).getString("id"));
                authPermissionExample.setOrderByClause("orders asc");
                List<AuthPermission> authPermissions = authPermissionMapper.selectByExample(authPermissionExample);
                if (authPermissions.size() == 0) {
                    continue;
                }
                // 目录
                JSONArray folders = new JSONArray();
                for (AuthPermission authPermission: authPermissions) {
                    if (!authPermission.getParentId().equals("0") || authPermission.getType() != 1) {
                        continue;
                    }
                    JSONObject node = new JSONObject();
                    node.put("id", authPermission.getId());
                    node.put("name", authPermission.getName());
                    node.put("open", true);
                    for (AuthRolePermission rolePermission : rolePermissions) {
                        if (rolePermission.getPermissionId().equals(authPermission.getId())) {
                            node.put("checked", true);
                        }
                    }
                    folders.add(node);
                    // 菜单
                    JSONArray menus = new JSONArray();
                    for (Object folder : folders) {
                        for (AuthPermission authPermission2: authPermissions) {
                            if (!authPermission2.getParentId().equals(((JSONObject) folder).getString("id")) || authPermission2.getType() != 2){
                                continue;
                            }
                            JSONObject node2 = new JSONObject();
                            node2.put("id", authPermission2.getId());
                            node2.put("name", authPermission2.getName());
                            node2.put("open", true);
                            for (AuthRolePermission rolePermission : rolePermissions) {
                                if (rolePermission.getPermissionId().equals(authPermission2.getId())) {
                                    node2.put("checked", true);
                                }
                            }
                            menus.add(node2);
                            // 按钮
                            JSONArray buttons = new JSONArray();
                            for (Object menu : menus) {
                                for (AuthPermission authPermission3: authPermissions) {
                                    if (!authPermission3.getParentId().equals (((JSONObject) menu).getString("id")) || authPermission3.getType() != 3){
                                        continue;
                                    }
                                    JSONObject node3 = new JSONObject();
                                    node3.put("id", authPermission3.getId());
                                    node3.put("name", authPermission3.getName());
                                    node3.put("open", true);
                                    for (AuthRolePermission rolePermission : rolePermissions) {
                                        if (rolePermission.getPermissionId().equals(authPermission3.getId())) {
                                            node3.put("checked", true);
                                        }
                                    }
                                    buttons.add(node3);
                                }
                                if (buttons.size() > 0) {
                                    ((JSONObject) menu).put("children", buttons);
                                    buttons = new JSONArray();
                                }
                            }
                        }
                        if (menus.size() > 0) {
                            ((JSONObject) folder).put("children", menus);
                            menus = new JSONArray();
                        }
                    }
                }
                if (folders.size() > 0) {
                    ((JSONObject) system).put("children", folders);
                }
            }
        }
        return systems;
    }

    @Override
    public JSONArray getTreeByUserId(String userId, Byte type) {
        // 用户权限
        AuthUserPermissionExample authUserPermissionExample = new AuthUserPermissionExample();
        authUserPermissionExample.createCriteria()
                .andUserIdEqualTo(userId)
                .andTypeEqualTo(type);
        List<AuthUserPermission> AuthUserPermissions = authUserPermissionMapper.selectByExample(authUserPermissionExample);

        JSONArray systems = new JSONArray();
        // 系统
        AuthSystemExample authSystemExample = new AuthSystemExample();
        authSystemExample.createCriteria()
                .andStatusEqualTo((byte) 1);
        authSystemExample.setOrderByClause("orders asc");
        List<AuthSystem> AuthSystems = authSystemMapper.selectByExample(authSystemExample);
        for (AuthSystem authSystem : AuthSystems) {
            JSONObject node = new JSONObject();
            node.put("id", authSystem.getId());
            node.put("name", authSystem.getTitle());
            node.put("nocheck", true);
            node.put("open", true);
            systems.add(node);
        }

        if (systems.size() > 0) {
            for (Object system: systems) {
                AuthPermissionExample authPermissionExample = new AuthPermissionExample();
                authPermissionExample.createCriteria()
                        .andStatusEqualTo((byte) 1)
                        .andSystemIdEqualTo(((JSONObject) system).getString("id"));
                authPermissionExample.setOrderByClause("orders asc");
                List<AuthPermission> authPermissions = authPermissionMapper.selectByExample(authPermissionExample);
                if (authPermissions.size() == 0){
                    continue;
                }
                // 目录
                JSONArray folders = new JSONArray();
                for (AuthPermission authPermission: authPermissions) {
                    if (!authPermission.getParentId().equals("0")|| authPermission.getType() != 1) {
                        continue;
                    }
                    JSONObject node = new JSONObject();
                    node.put("id", authPermission.getId());
                    node.put("name", authPermission.getName());
                    node.put("open", true);
                    for (AuthUserPermission AuthUserPermission : AuthUserPermissions) {
                        if (AuthUserPermission.getPermissionId().equals(authPermission.getId())) {
                            node.put("checked", true);
                        }
                    }
                    folders.add(node);
                    // 菜单
                    JSONArray menus = new JSONArray();
                    for (Object folder : folders) {
                        for (AuthPermission authPermission2: authPermissions) {
                            if (!authPermission2.getParentId().equals(((JSONObject) folder).getString("id")) || authPermission2.getType() != 2) {
                                continue;
                            }
                            JSONObject node2 = new JSONObject();
                            node2.put("id", authPermission2.getId());
                            node2.put("name", authPermission2.getName());
                            node2.put("open", true);
                            for (AuthUserPermission AuthUserPermission : AuthUserPermissions) {
                                if (AuthUserPermission.getPermissionId().equals(authPermission2.getId())) {
                                    node2.put("checked", true);
                                }
                            }
                            menus.add(node2);
                            // 按钮
                            JSONArray buttons = new JSONArray();
                            for (Object menu : menus) {
                                for (AuthPermission authPermission3: authPermissions) {
                                    if (!authPermission3.getParentId().equals(((JSONObject) menu).getIntValue("id")) || authPermission3.getType() != 3) {
                                        continue;
                                    }
                                    JSONObject node3 = new JSONObject();
                                    node3.put("id", authPermission3.getId());
                                    node3.put("name", authPermission3.getName());
                                    node3.put("open", true);
                                    for (AuthUserPermission AuthUserPermission : AuthUserPermissions) {
                                        if (AuthUserPermission.getPermissionId().equals(authPermission3.getId())) {
                                            node3.put("checked", true);
                                        }
                                    }
                                    buttons.add(node3);
                                }
                                if (buttons.size() > 0) {
                                    ((JSONObject) menu).put("children", buttons);
                                    buttons = new JSONArray();
                                }
                            }
                        }
                        if (menus.size() > 0) {
                            ((JSONObject) folder).put("children", menus);
                            menus = new JSONArray();
                        }
                    }
                }
                if (folders.size() > 0) {
                    ((JSONObject) system).put("children", folders);
                }
            }
        }
        return systems;
    }
}
