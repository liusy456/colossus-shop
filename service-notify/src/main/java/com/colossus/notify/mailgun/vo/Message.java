package com.colossus.notify.mailgun.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Message {

    private String from;

    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    private String subject;

    private String textBody;

    private String htmlBody;

    private List<Attachment> attachments;

    private List<Attachment> inlines;

    private String oTag;

    private String oCampaign;

    private Boolean oDkim;

    private Date oDeliverytime;

    private Boolean oTestmode;

    private Boolean oTracking;

    private String oTrackingClicks;

    private Boolean oTrackingOpens;

    private Boolean oRequireTls;

    private Boolean oSkipVerification;

    private Map<String,String> hXMyHeader;

    private Map<String,String> vMyVar;

    private String recipientVariables;


}
