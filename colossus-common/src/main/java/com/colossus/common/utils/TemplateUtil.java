package com.colossus.common.utils;


import com.colossus.common.exception.ServiceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Tlsy1
 * @since 2018-11-24 15:01
 **/
public class TemplateUtil {

    private static Configuration cfg;
    static {
        cfg = new Configuration(Configuration.VERSION_2_3_0);
        cfg.setClassForTemplateLoading(TemplateUtil.class, "/template");
        cfg.setDefaultEncoding("utf-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
    }

    public static String process(Object model, String templateFile) {
        try {
            Template template = cfg.getTemplate(templateFile);
            StringWriter out = new StringWriter();
            template.process(model, out);
            out.close();
            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new ServiceException("使用模板格式化数据失败",e);
        }
    }
}
