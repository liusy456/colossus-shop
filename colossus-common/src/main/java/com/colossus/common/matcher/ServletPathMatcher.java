package com.colossus.common.matcher;

import org.apache.shiro.util.PatternMatcher;

/**
 * @author Tlsy1
 * @since 2018-12-07 15:19
 **/
public class ServletPathMatcher implements PatternMatcher {
    private static final ServletPathMatcher INSTANCE = new ServletPathMatcher();

    public ServletPathMatcher() {
    }

    public static ServletPathMatcher getInstance() {
        return INSTANCE;
    }

    public boolean matches(String pattern, String source) {
        if (pattern != null && source != null) {
            pattern = pattern.trim();
            source = source.trim();
            int start;
            if (pattern.endsWith("*")) {
                start = pattern.length() - 1;
                if (source.length() >= start && pattern.substring(0, start).equals(source.substring(0, start))) {
                    return true;
                }
            } else if (pattern.startsWith("*")) {
                start = pattern.length() - 1;
                if (source.length() >= start && source.endsWith(pattern.substring(1))) {
                    return true;
                }
            } else if (pattern.contains("*")) {
                start = pattern.indexOf("*");
                int end = pattern.lastIndexOf("*");
                if (source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1))) {
                    return true;
                }
            } else if (pattern.equals(source)) {
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

}
