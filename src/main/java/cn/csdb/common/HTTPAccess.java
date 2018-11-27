package cn.csdb.common;

import cn.csdb.utils.HttpUtils;
import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhijian on 2016/4/28.
 */
@Component
public class HTTPAccess {

    private static String uriRank;
    private static String uriGroup;
    private static String GET_ROLES;
    private static String GET_USER_INFO;
    private static String GET_COI;

    public static String getRoles(String loginId) {
        if (Strings.isNullOrEmpty(loginId)) {
            return null;
        }
        String rolesUrl = GET_ROLES;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("loginId", loginId));
        return HttpUtils.getInfoByGet(rolesUrl, params, 2000);

    }

    public static String getUserInfo(String loginId) {
        if (Strings.isNullOrEmpty(loginId)) {
            return null;
        }
        String userInfoUrl = GET_USER_INFO;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("loginId", loginId));
        return HttpUtils.getInfoByGet(userInfoUrl, params, 2000);
    }

    public static String getCOI(String loginId) {
        String rolesUrl = GET_COI;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("loginId", loginId));
        return HttpUtils.getInfoByGet(rolesUrl, params, 2000);
    }

    public static String findAllRanks() {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet getRank = new HttpGet(uriRank);
        CloseableHttpResponse response = null;
        String rankResult = "";
        try {
            response = httpClient.execute(getRank);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                rankResult = IOUtils.toString(inputStream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rankResult;
    }

    public static String findAllGroups() {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet getGroup = new HttpGet(uriGroup);
        CloseableHttpResponse response = null;
        String groupResult = "";
        try {
            response = httpClient.execute(getGroup);
            HttpEntity entity = response.getEntity();
            response = httpClient.execute(getGroup);
            entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                groupResult = IOUtils.toString(inputStream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupResult;
    }


}
