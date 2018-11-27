package cn.csdb.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhijian on 2016/4/27.
 */
public class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};

    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal))
                return true;
        }
        return false;
    }

    public static boolean isWindows(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("windows") || userAgent.contains("Windows"))
            return true;
        return false;
    }

    public static boolean isLinux(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("linux") || userAgent.contains("Linux"))
            return true;
        return false;
    }

    public static void produceError(HttpServletRequest request, HttpServletResponse response, int code) throws IOException, ServletException {
        String requestType = request.getHeader("X-Requested-With");
        //xiajl20170421修改
        String acceptType=request.getHeader("Accept");

        if ("XMLHttpRequest".equalsIgnoreCase(requestType) || "application/json".equalsIgnoreCase(acceptType)) {
            Result result = new Result();
            result.getHead().setCode(code);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSONObject.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            request.getRequestDispatcher("/" + String.valueOf(code)).forward(request, response);
        }
    }
    public static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }
    public static String getInfoByGet(String url, List<BasicNameValuePair> params, int timeout) {

        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        StringBuilder requestUrl = new StringBuilder(url);
        if (params != null) {
            String paramsStr = URLEncodedUtils.format(params, "utf-8");
            requestUrl.append("?");
            requestUrl.append(paramsStr);
        }
        HttpGet httpGet = new HttpGet(requestUrl.toString());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        InputStream inputStream = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);//执行请求
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            inputStream = entity.getContent();
            String roleResult = IOUtils.toString(inputStream, "UTF-8");
            if (!Strings.isNullOrEmpty(roleResult)) {
                return roleResult;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;

    }


    //xiajl20170414增加 post方法获取
    public static String getInfoByPost(String url, List<BasicNameValuePair> params, int timeout) {

        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        StringBuilder requestUrl = new StringBuilder(url);
        if (params != null) {
            String paramsStr = URLEncodedUtils.format(params, "utf-8");
            requestUrl.append("?");
            requestUrl.append(paramsStr);
        }
        //HttpGet httpGet = new HttpGet(requestUrl.toString());
        HttpPost httpPost = new HttpPost(requestUrl.toString());

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        InputStream inputStream = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            inputStream = entity.getContent();
            String roleResult = IOUtils.toString(inputStream, "UTF-8");
            if (!Strings.isNullOrEmpty(roleResult)) {
                return roleResult;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

}
