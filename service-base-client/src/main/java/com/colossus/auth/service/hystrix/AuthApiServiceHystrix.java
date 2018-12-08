package com.colossus.auth.service.hystrix;

import com.colossus.auth.service.AuthApiService;
import com.colossus.common.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthApiServiceHystrix implements AuthApiService {
    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserId(String authUserId) {
        return null;
    }

    @Override
    public List<AuthPermission> selectAuthPermissionByAuthUserIdByCache(String authUserId) {
        return null;
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserId(String authUserId) {
        return null;
    }

    @Override
    public List<AuthRole> selectAuthRoleByAuthUserIdByCache(String authUserId) {
        return null;
    }

    @Override
    public List<AuthRolePermission> selectAuthRolePermissionByAuthRoleId(String authRoleId) {
        return null;
    }

    @Override
    public List<AuthUserPermission> selectAuthUserPermissionByAuthUserId(String authUserId) {
        return null;
    }

    @Override
    public List<AuthSystem> selectAuthSystemByExample(AuthSystemExample authSystemExample) {
        return null;
    }

    @Override
    public List<AuthOrganization> selectAuthOrganizationByExample(AuthOrganizationExample authOrganizationExample) {
        return null;
    }

    @Override
    public AuthUser selectAuthUserByUsername(String username) {
        return null;
    }

}
