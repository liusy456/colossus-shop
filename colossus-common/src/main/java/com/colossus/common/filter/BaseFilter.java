package com.colossus.common.filter;

import com.colossus.common.matcher.ServletPathMatcher;
import org.apache.shiro.util.PatternMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Tlsy1
 * @since 2018-12-07 15:16
 **/
public abstract class BaseFilter implements Filter {
    private String contextPath;
    private PatternMatcher pathMatcher = new ServletPathMatcher();
    private Set<String> excludesPattern;

    private String getContextPath_2_5(ServletContext context) {
        String contextPath = context.getContextPath();
        if (contextPath == null || contextPath.length() == 0) {
            contextPath = "/";
        }
        return contextPath;
    }

    private String getContextPath(ServletContext context) {
        if (context.getMajorVersion() == 2 && context.getMinorVersion() < 5) {
            return null;
        } else {
            try {
                return getContextPath_2_5(context);
            } catch (NoSuchMethodError var2) {
                return null;
            }
        }
    }
    public boolean isExclusion(String requestURI) {
        if (this.excludesPattern != null && requestURI != null) {
            if (this.contextPath != null && requestURI.startsWith(this.contextPath)) {
                requestURI = requestURI.substring(this.contextPath.length());
                if (!requestURI.startsWith("/")) {
                    requestURI = "/" + requestURI;
                }
            }

            Iterator var2 = this.excludesPattern.iterator();

            String pattern;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                pattern = (String)var2.next();
            } while(!this.pathMatcher.matches(pattern, requestURI));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String param = config.getInitParameter("exclusions");
        if (param != null && param.trim().length() != 0) {
            this.excludesPattern = new HashSet(Arrays.asList(param.split("\\s*,\\s*")));
        }
        this.contextPath = getContextPath(config.getServletContext());
    }

    @Override
    public void destroy() {
    }
}
