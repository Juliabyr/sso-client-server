package com.unionpay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionpay.info.Auth;
import com.unionpay.info.VerifyBean;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class HttpUtil {

    public HttpResponse httpGet(String requestURL) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestURL);
        String responseBody = "";
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
//            responseBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                throw new ClientProtocolException("Unexpected response status: " + statusCode);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponse;
    }


    public static void main(String[] args) throws IOException {
        HttpUtil httpUtil = new HttpUtil();
        HttpResponse httpResponse = httpUtil.httpGet("http://localhost:8080/server/auth/verify?ticket=54a5c6d0998c478290b5cc654c176401");
        System.out.println(EntityUtils.toString(httpResponse.getEntity(),
                "UTF-8"));
//        int statusCode = httpResponse.getStatusLine().getStatusCode();
//        try {
//            if (statusCode == HttpStatus.SC_OK) {
//                String responseBody = null;
////                responseBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
//                responseBody = EntityUtils.toString(httpResponse.getEntity(),
//                        "UTF-8");
//                ObjectMapper objectMapper = new ObjectMapper();
//                VerifyBean verifyResult = objectMapper.readValue(responseBody, VerifyBean.class);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        }


}

