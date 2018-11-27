package cn.csdb.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        } else {
            int count = values.length;
            String[] encodedValues = new String[count];

            for(int i = 0; i < count; ++i) {
                encodedValues[i] = this.cleanXSS(values[i]);
            }

            return encodedValues;
        }
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return value == null ? null : this.cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : this.cleanXSS(value);
    }

    private String cleanXSS(String value) {
        //System.out.println("\n=============没有过滤之前的内容:" + value);
        //value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        //value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        //value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("alert", "");
        value = value.replaceAll("eval", "");

        //System.out.println("\n=============20180607过滤之后的内容:" + value);
        return value;
    }
}