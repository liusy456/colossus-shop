package com.colossus.auth.service.impl;


import com.alibaba.fastjson.JSON;
import com.colossus.auth.service.SSOService;
import com.colossus.auth.shiro.CustomUsernamePasswordToken;
import com.colossus.common.dao.AuthUserMapper;
import com.colossus.common.model.AuthUser;
import com.colossus.common.model.AuthUserExample;
import com.colossus.common.model.BaseResult;
import com.colossus.common.utils.AppUtil;
import com.colossus.common.utils.FastJsonConvert;
import com.colossus.common.utils.StringUtil;
import com.colossus.redis.service.RedisService;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户单点登录相关服务 Service 实现
 *
 * @author Tlsy.
 * @create 2017-05-05
 */
@Api(value = "SSO-service", description = "sso 服务")
@RestController
@RefreshScope
public class SSOServiceImpl implements SSOService {

    private static final Logger logger = LoggerFactory.getLogger(SSOServiceImpl.class);
    public static final String KEY = "success";
    public static final int ERROR = 1;
    public static final int SUCCESS = 0;

    @Autowired
    private AuthUserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Value("${redisKey.shiro.user_cache}")
    private String USER_CACHE;

    @Value("${redisKey.shiro.session_cache}")
    private String SESSION_CACHE;

    @Value("${redisKey.expire_time}")
    private Integer EXPIRE_TIME;
    
    @Value("${login.validation.ispinengaged}")
    private String ISPINENGAGED;
    
    @Value("${login.validation.isemailengaged}")
    private String ISEMAILENGAGED;
    
    @Value("${login.validation.ismobileengaged}")
    private String ISMOBILEENGAGED;

    @Value("${login.random_number}")
    private Integer RANDOM_NUMBER;

    @Value("${redisKey.verifycode}")
    private String VERIFY_CODE;

    @Value("${redisKey.mobile_login_code}")
    private String MOBILE_LOGIN_CODE;

    @Value("${login.success_url}")
    private String SUCCESS_URL;



    /**
     * 请求格式 POST
     * 用户登录
     * @return {
     *          status: 200 //200 成功 400 登录失败 500 系统异常
     *          msg: "OK" //错误 用户名或密码错误,请检查后重试.
     *          data: "fe5cb546aeb3ce1bf37abcb08a40493e" //登录成功，返回token
     *         }
     */
    @Override
    @ApiOperation("用户登录")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "user", value = "", required = true, dataType = "User"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public BaseResult login(String username,String password,boolean rememberMe,HttpServletRequest request){


        if (org.apache.commons.lang.StringUtils.isBlank(username)) {
            return BaseResult.build(400, "帐号不能为空！");
        }
        if (org.apache.commons.lang.StringUtils.isBlank(password)) {
            return BaseResult.build(400, "密码不能为空！");
        }
        Subject subject = SecurityUtils.getSubject();


        // 使用shiro认证
        String host = StringUtil.getRemoteAddr(request);
        CustomUsernamePasswordToken usernamePasswordToken=new CustomUsernamePasswordToken(username,password.toCharArray(),rememberMe,host);
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            return BaseResult.build(403, "帐号不存在！");
        } catch (IncorrectCredentialsException e) {
            return  BaseResult.build(403, "密码错误！");
        } catch (LockedAccountException e) {
            return BaseResult.build(403, "帐号已锁定！");
        }
        AuthUserExample example=new AuthUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        if(CollectionUtils.isEmpty(userMapper.selectByExample(example))){
            return BaseResult.build(403, "帐号不存在！");
        }
        AuthUser user=userMapper.selectByExample(example).get(0);
        // 默认验证帐号密码正确，创建token
        String token = AppUtil.getUUID();
        // 全局会话的code
        redisService.set(USER_CACHE+token, JSON.toJSONString(user));

        return BaseResult.build(200,"success",token);

        //跳登录前地址

    }

    /**
     * 请求格式 GET
     * 根据token值获取用户信息
     *
     * @param token    token值
     * @param callback 可选参数 有参表示jsonp调用
     * @return {
     *          status: 200 //200 成功 400 没有此token 500 系统异常
     *          msg: "OK" //错误 没有此token.
     *          data: {"username":"xbin","id":"id"} //返回用户名
     *         }
     */
    @Override
    @ApiOperation("获取token")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "token", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "callback", value = "", required = true, dataType = "String"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public BaseResult token(String token, String callback) {

        if (StringUtils.isNotBlank(callback)) {
            return BaseResult.ok(callback); // 未处理
        }

        try {
            String user = redisService.get(USER_CACHE + token);

            if (StringUtils.isNotBlank(user)) {

                return BaseResult.ok(user);
            }

        } catch (Exception e) {

            logger.error("Redis服务出错");

        }

        return BaseResult.build(400, "没有此用户");
    }

    /**
     * 请求格式 GET
     * 根据token值 退出登录
     *
     * @param token    token值
     * @param callback 可选参数 有参表示jsonp调用
     * @return {
     *          status: 200 //200 成功 400 没有此token 500 系统异常
     *          msg: "OK" //错误 没有此token.
     *          data: null
     *         }
     */
    @Override
    @ApiOperation("退出登录")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "token", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "callback", value = "", required = true, dataType = "String"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public BaseResult logout(String token, String callback) {

        if (StringUtils.isNotBlank(callback)) {
            return BaseResult.ok(callback); // 未处理
        }

        try {
            redisService.del(USER_CACHE + token);
        } catch (Exception e) {
            logger.error("没有登录", e);

            return BaseResult.build(400, "没有登录");
        }

        return BaseResult.ok();
    }

    /**
     * 请求格式 POST
     * 注册检查是否可用
     *
     * @param isEngaged 需要检查是否使用的名称
     * @return {
     *          "success": 0 可用 1 不可用
     *          "morePin":["sssss740","sssss5601","sssss76676"] //isEngaged = isPinEngaged时返回推荐
     *         }
     */
    @Override
    @ApiOperation("注册检查是否可用")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "isEngaged", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "regName", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "email", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "phone", value = "", required = true, dataType = "String"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public String validateUser(String isEngaged,String regName,String email,String phone) {

        Random random = new Random();

        HashMap<String, Object> map = new HashMap<>();

        AuthUserExample example = new AuthUserExample();

        AuthUserExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(isEngaged)) {

            if (isEngaged.equals(ISPINENGAGED) && StringUtils.isNotBlank(regName)) {

                criteria.andUsernameEqualTo(regName);

                List<AuthUser> users = userMapper.selectByExample(example);

                if (users == null || users.size() == 0) {
                    //用户名 可用
                    map.put(KEY, 0);

                    return FastJsonConvert.convertObjectToJSON(map);
                }

                //用户名 不可用
                map.put(KEY, ERROR);
                ArrayList<String> morePin = new ArrayList<>();
                morePin.add(regName + random.nextInt(RANDOM_NUMBER));
                morePin.add(regName + random.nextInt(RANDOM_NUMBER));
                morePin.add(regName + random.nextInt(RANDOM_NUMBER));
                // 不考虑生成的用户名继续重名
                map.put("morePin", morePin);

                return FastJsonConvert.convertObjectToJSON(map);

            } else {
                if (isEngaged.equals(ISEMAILENGAGED) && StringUtils.isNotBlank(email)) {

                    criteria.andEmailEqualTo(email);

                    List<AuthUser> users = userMapper.selectByExample(example);

                    if (users == null || users.size() == 0) {
                        //email 可用
                        map.put(KEY, 0);

                        return FastJsonConvert.convertObjectToJSON(map);
                    }
                    //email 不可用
                    map.put(KEY, ERROR);

                    return FastJsonConvert.convertObjectToJSON(map);


                } else if (isEngaged.equals(ISMOBILEENGAGED) && StringUtils.isNotBlank(phone)) {

                    criteria.andPhoneEqualTo(phone);

                    List<AuthUser> users = userMapper.selectByExample(example);

                    if (users == null || users.size() == 0) {
                        //phone 可用
                        map.put(KEY, 0);

                        return FastJsonConvert.convertObjectToJSON(map);
                    }
                    //phone 不可用
                    map.put(KEY, ERROR);

                    return FastJsonConvert.convertObjectToJSON(map);
                }


            }

        }

        logger.error("传递类型出错！");
        map.put("error", ERROR);

        return FastJsonConvert.convertObjectToJSON(map);
    }

    /**
     * 请求格式 POST
     * 验证验证码
     *
     * @param authCode 输入的验证码
     * @param uuid     Redis验证码uuid
     * @return {
     *          "success": 0 可用 1 不可用
     *         }
     */
    @Override
    @ApiOperation("验证验证码")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "authCode", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "String"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public String validateAuthCode(String authCode, String uuid) {

        HashMap<String, Integer> map = new HashMap<>();

        try {
            String redisAuthCode = redisService.get(VERIFY_CODE + uuid);

            if (StringUtils.isBlank(redisAuthCode)) {

                map.put(KEY, ERROR);

                logger.info("Redis中根据key查询不到");

                return FastJsonConvert.convertObjectToJSON(map);
            }

            if (StringUtils.isBlank(authCode)) {
                map.put(KEY, ERROR);

                logger.info("验证码为空");

                return FastJsonConvert.convertObjectToJSON(map);
            }

            if (redisAuthCode.equalsIgnoreCase(authCode)) {

                map.put(KEY, SUCCESS);

                return FastJsonConvert.convertObjectToJSON(map);
            }


        } catch (Exception e) {

            logger.error("redis 服务出错", e);

        }

        map.put(KEY, ERROR);

        return FastJsonConvert.convertObjectToJSON(map);
    }

    /**
     * 请求格式 POST
     * 注册
     *
     * @param regName       注册名
     * @param pwd           第一次密码
     * @param pwdRepeat     第二次密码
     * @param phone         电话
     * @param mobileCode    手机验证码
     * @param email         邮箱
     * @param authCode      输入的验证码
     * @param uuid          Redis验证码uuid
     * @return
     */
    @Override
    @ApiOperation("注册")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "regName", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "pwd", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "pwdRepeat", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "phone", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "mobileCode", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "authCode", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "email", value = "", required = true, dataType = "String"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public String register(String regName, String pwd, String pwdRepeat, String phone, String mobileCode, String uuid, String authCode, String email) {

        if (!pwd.equals(pwdRepeat)) {

            String info = "两次密码不正确";
            return "({'info':'" + info + "'})";

        }

        if (StringUtils.isNotBlank(authCode)) {

            String code = "";
            try {
                code = redisService.get(VERIFY_CODE + uuid);
            } catch (Exception e) {

                logger.error("Redis服务出错", e);

            }
            //if (StringUtils.isBlank(code)) {
            //    String info = "验证码不正确或已过期，请重新获取";
            //    String convert = ConvertUtils.convert(info);
            //    return "({'info':'" + convert + "'})";
            //}

            if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(authCode)) {

                String info = "验证码不正确或已过期，请重新获取";
                return "({'info':'" + info + "'})";

            }

        } else {

            String info = "验证码不能为空";
            return "({'info':'" + info + "'})";

        }

        if (StringUtils.isNotBlank(phone)) {
            String phone2 = phone.substring(5, phone.length());
            String phonecode = "";
            try {
                phonecode = redisService.get(MOBILE_LOGIN_CODE + phone2);
            } catch (Exception e) {
                logger.error("Redis服务出错");
            }

//            if (StringUtils.isBlank(phonecode) || !phonecode.equals(mobileCode)) {
//
//                String info = "短信验证码不正确或已过期,请重新获取";
//                return "({'info':'" + info + "'})";
//
//            }
        } else {
            String info = "手机号码不能为空";
            return "({'info':'" + info + "'})";
        }

        if (StringUtils.isNotBlank(regName)) {

            AuthUser user = new AuthUser();
            user.setId(AppUtil.getUUID());
            user.setUsername(regName);
            user.setPassword(AppUtil.entryptPassword(pwd));
            user.setPhone(phone);

            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());

            if (StringUtils.isNotBlank(email)) {
                user.setEmail(email);
            }

            userMapper.insert(user);
            //注册成功 忽略noAuth这个词
            return "({'noAuth':'" + SUCCESS_URL + "?username=" + regName + "'})";
        }
        //注册失败
        return "({'error':1})";


    }
}
