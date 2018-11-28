package com.colossus.common.utils;


import com.colossus.common.code_enum.RequestMethodEnum;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 系统通用工具类
 * @author Tlsy
 * @version commerce 0.0.1
 * @date 2017/4/19  14:31
 */
public class AppUtil {

    /**
     * 调用远程接口
     * @param url
     * @param method
     * @param params
     * @param auth
     * @return
     */
    public static String httpRequestToString(String url, RequestMethodEnum method, Map<String, String> params, String ...auth){
        //接口返回结果
        String result = null;
        try {
            //将参数集合拼接成特定格式
            List<NameValuePair> paramPairs = new ArrayList<>();
            for (Map.Entry<String, String> e : params.entrySet()) {
                String name = e.getKey();
                String value = e.getValue();
                NameValuePair pair = new BasicNameValuePair(name, value);
                paramPairs.add(pair);
            }

            RequestConfig requestConfig= RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();

            //创建HttpClient连接对象
            HttpClientBuilder builder= HttpClientBuilder.create();
            CloseableHttpClient client=builder.build();
            HttpUriRequest request = null;
            if(RequestMethodEnum.GET==method){
                request= RequestBuilder.get().addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addParameters(paramPairs.toArray(new BasicNameValuePair[paramPairs.size()]))
                        .setConfig(requestConfig)
                        .setUri(url)
                        .build();
            }else if(RequestMethodEnum.POST==method){
                request= RequestBuilder.post().addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addParameters(paramPairs.toArray(new BasicNameValuePair[paramPairs.size()]))
                        .setConfig(requestConfig)
                        .setUri(url)
                        .build();
            }else if(RequestMethodEnum.PUT==method){
                request= RequestBuilder.put().addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addParameters(paramPairs.toArray(new BasicNameValuePair[paramPairs.size()]))
                        .setConfig(requestConfig)
                        .setUri(url)
                        .build();
            }else if(RequestMethodEnum.DELETE==method){
                request= RequestBuilder.delete().addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addParameters(paramPairs.toArray(new BasicNameValuePair[paramPairs.size()]))
                        .setConfig(requestConfig)
                        .setUri(url)
                        .build();
            }
            //httpClient本地上下文
            HttpClientContext context = null;
            if(!(auth==null || auth.length==0)){
                String username = auth[0];
                String password = auth[1];
                UsernamePasswordCredentials credit = new UsernamePasswordCredentials(username,password);
                //凭据提供器
                CredentialsProvider provider = new BasicCredentialsProvider();
                //凭据的匹配范围
                provider.setCredentials(AuthScope.ANY, credit);
                context = HttpClientContext.create();
                context.setCredentialsProvider(provider);
            }
            //访问接口，返回状态码
            HttpResponse response = client.execute(request, context);
            //返回状态码200，则访问接口成功
            if(response.getStatusLine().getStatusCode()==200){
                result = EntityUtils.toString(response.getEntity());
            }
            client.close();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成随机UUID
     * @return
     */
    public static String getUUID(){
        java.util.UUID uuid = java.util.UUID.randomUUID();
        if (uuid.toString().length() <= 32) {
            return uuid.toString();
        } else {
            return uuid.toString().replace("-", "");
        }
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword) {
        String plain = StringUtil.unescapeHtml(plainPassword);
        byte[] salt = HashUtil.generateSalt(AppConfig.SALT_SIZE);
        byte[] hashPassword = HashUtil.sha1(plain.getBytes(),salt,AppConfig.HASH_INTERATIONS);
        return StringUtil.encodeHex(salt)+StringUtil.encodeHex(hashPassword);
    }
}
