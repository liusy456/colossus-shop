package com.colossus.notify.mailgun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colossus.common.exception.ServiceException;
import com.colossus.notify.mailgun.vo.Attachment;
import com.colossus.notify.mailgun.vo.Event;
import com.colossus.notify.mailgun.vo.Message;
import com.colossus.notify.mailgun.vo.StoreMail;
import com.colossus.notify.mailgun.vo.event.EventPage;
import com.colossus.notify.mailgun.vo.event.ItemsItem;
import com.google.common.collect.Lists;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Tlsy1
 * @since 2019-01-16 16:19
 **/
@Component
public class MailGunClient implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MailGunClient.class);

    private OkHttpClient okHttpClient;

    @Value("${spring.mailgun.domain}")
    protected String domain;
    @Value("${spring.mailgun.api-key}")
    protected String apiKey;
    @Value("${spring.mailgun.api-url}")
    protected String apiUrl = "https://api.mailgun.net/v3/";

    public String sendMessage(Message msg) {
        MultipartBody multipartBody = createSendEmailBody(msg);
        Request httpRequest = new Request.Builder().url(createApiUrl("messages")).post(multipartBody).build();
        try {
            Response response = okHttpClient.newCall(httpRequest).execute();
            JSONObject result = JSONObject.parseObject(response.body().string());
            return result.getString("id").replaceAll("[<>]","");
        }catch (Exception e) {
            throw new ServiceException("邮件发送失败:"+msg,e);
        }
    }


    public List<ItemsItem> queryStoreEvent(Date begin,Date end ,boolean ascending,int limit,Map<Event.FilterFiledEnum, String> filter) {
        logger.info("开始查询存储邮件begin:{},end:{},filter:{}", begin, end, filter);
        List<ItemsItem> result = Lists.newArrayList();
        try {
            Request httpRequest = new Request.Builder().url(createEventsRequest(begin,end,ascending,limit,filter)).build();
            queryEventPage(result, httpRequest);
            return result;
        } catch (Exception e) {
            throw new ServiceException("查询邮件事件失败",e);
        }
    }


    public StoreMail queryStoreMail(String storeUrl){
        Request httpRequest = new Request.Builder().url(storeUrl).build();
        StoreMail storeMail = null;
        try {
            Response response = okHttpClient.newCall(httpRequest).execute();
            if(response.isSuccessful()){
                storeMail = JSON.parseObject(response.body().string(), StoreMail.class);
            }
            return storeMail;
        }catch (Exception e) {
            throw new ServiceException("邮件查询失败",e);
        }
    }
    private void queryEventPage(List<ItemsItem> result, Request httpRequest) throws IOException {
        Response response = okHttpClient.newCall(httpRequest).execute();
        if(response.isSuccessful()){
            EventPage eventPage = JSON.parseObject(response.body().string(),EventPage.class);
            if(CollectionUtils.isNotEmpty(eventPage.getItems())){
                result.addAll(eventPage.getItems());
                queryEventByNext(eventPage.getPaging().getNext(),result);
            }
        }
    }

    private void queryEventByNext(String next,List<ItemsItem> result) {
        try {
            Request httpRequest = new Request.Builder().url(next).build();
            queryEventPage(result, httpRequest);
        } catch (Exception e) {
            logger.error("查询event出错：{}", e);
        }
    }

    private String createApiUrl(String method) {
        return apiUrl+this.domain+"/"+(method);
    }

    private String  DateToTimestamp(Date time){
        Timestamp ts = new Timestamp(time.getTime());
        return String.valueOf ((ts.getTime())/1000);
    }

    private String createEventsRequest(Date begin, Date end, Boolean ascending, Integer limit, Map<Event.FilterFiledEnum, String> filter) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(createApiUrl("events"));
        if(begin!=null) {
            uriBuilder.addParameter("begin",DateToTimestamp(begin));
        }
        if(end!=null){
            uriBuilder.addParameter("end",DateToTimestamp(end));
        }
        if(ascending!=null){
            uriBuilder.addParameter("ascending",ascending?"yes":"no");
        }
        if(limit!=null){
            uriBuilder.addParameter("limit",limit.toString());
        }
        if(filter!=null){
            for(Map.Entry<Event.FilterFiledEnum,String> entry:filter.entrySet()){
                uriBuilder.addParameter(entry.getKey().getFieldName(),entry.getValue());
            }
        }
        return uriBuilder.build().toString();
    }
    private MultipartBody createSendEmailBody(Message msg) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if(msg.getODkim()!=null){
            builder.addFormDataPart("o:dkim",msg.getODkim()?"yes":"no");
        }
        if(msg.getORequireTls()!=null){
            builder.addFormDataPart("o:require-tls",msg.getORequireTls()?"True":"False");
        }
        if(msg.getOSkipVerification()!=null){
            builder.addFormDataPart("o:skip-verification",msg.getOSkipVerification()?"True":"False");
        }
        if(msg.getOTestmode()!=null && msg.getOTestmode()){
            builder.addFormDataPart("o:testmode","true");
        }
        if(msg.getOTracking()!=null){
            builder.addFormDataPart("o:tracking",msg.getOTracking()?"yes":"no");
        }
        if(msg.getOTrackingOpens()!=null){
            builder.addFormDataPart("o:tracking-opens",msg.getOTrackingOpens()?"yes":"no");
        }
        if(msg.getODeliverytime()!=null){
            builder.addFormDataPart("o:deliverytime",formatDate(msg.getODeliverytime()));
        }
        if(StringUtils.isNotEmpty(msg.getOCampaign())){
            builder.addFormDataPart("o:campaign",msg.getOCampaign());
        }
        if(StringUtils.isNotEmpty(msg.getOTag())){
            builder.addFormDataPart("o:tag",msg.getOTag());
        }
        if(StringUtils.isNotEmpty(msg.getOTrackingClicks())){
            builder.addFormDataPart("o:tracking-clicks",msg.getOTrackingClicks());
        }
        if(msg.getFrom()==null){
            throw new ServiceException("发件人不能为空");
        }else{
            builder.addFormDataPart("from",msg.getFrom());
        }
        if(CollectionUtils.isNotEmpty(msg.getTo())){
            for(String addr:msg.getTo()){
                builder.addFormDataPart("to", addr);
            }
        }
        if(StringUtils.isNotEmpty(msg.getRecipientVariables())){
            builder.addFormDataPart("recipient-variables",msg.getRecipientVariables());
        }
        if(CollectionUtils.isNotEmpty(msg.getCc())){
            for(String addr:msg.getCc()){
                builder.addFormDataPart("cc",addr);
            }
        }
        if(CollectionUtils.isNotEmpty(msg.getBcc())){
            for(String addr:msg.getBcc()){
                builder.addFormDataPart("bcc",addr);
            }
        }
        if(StringUtils.isNotEmpty(msg.getSubject())){
            builder.addFormDataPart("subject",msg.getSubject());
        }
        if(StringUtils.isNotEmpty(msg.getTextBody())) {
            builder.addFormDataPart("text", msg.getTextBody());
        }
        if(StringUtils.isNotEmpty(msg.getHtmlBody())) {
            builder.addFormDataPart("html", msg.getHtmlBody());
        }
        if(CollectionUtils.isNotEmpty(msg.getAttachments())){
            for(Attachment file:msg.getAttachments()){
                RequestBody fileBody = RequestBody.create(MediaType.parse("text/*"),file.getFilePath());
                builder.addFormDataPart("attachment",encodeFileName(file.getFileName()),fileBody);
            }
        }
        if(CollectionUtils.isNotEmpty(msg.getInlines())){
            for(Attachment file:msg.getInlines()){
                RequestBody fileBody = RequestBody.create(MediaType.parse("text/*"),file.getFilePath());
                builder.addFormDataPart("inline",encodeFileName(file.getFileName()),fileBody);
            }
        }

        if(msg.getHXMyHeader()!=null){
            for(Map.Entry<String,String> entry: msg.getHXMyHeader().entrySet()){
                builder.addFormDataPart("h:"+entry.getKey(),entry.getValue());
            }
        }
        if(msg.getVMyVar()!=null){
            for(Map.Entry<String,String> entry: msg.getVMyVar().entrySet()){
                builder.addFormDataPart("v:"+entry.getKey(),entry.getValue());
            }
        }
        return builder.build();
    }

    private String formatDate(Date date) {
        return DateFormatUtils.format(date,"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    private String encodeFileName(String filename) {
        try {
            return MimeUtility.encodeText(filename, "utf-8", null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor("api", apiKey))
                .build();
    }
}
