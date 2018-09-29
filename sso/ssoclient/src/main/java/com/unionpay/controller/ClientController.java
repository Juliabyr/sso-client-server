package com.unionpay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionpay.info.Auth;
import com.unionpay.info.TokenInfo;
import com.unionpay.info.VerifyBean;
import com.unionpay.session.LocalSessions;
import com.unionpay.util.HttpUtil;
import com.unionpay.util.TokenUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@RequestMapping("/client")
@Controller
public class ClientController {

    @Resource
    HttpUtil httpUtil;
    @Resource
    TokenUtils tokenUtils;
    @Value("${server.url.add}")
    private String serverUrlAddr;

    /**
     * 1. 直接访问系统应用时，如果局部会话中存在，则直接返回main页面
     * 2. 直接访问系统应用时，如果没有局部会话，则http重定向去认证中心认证
     * 3. 从认证中心重定向回来时，携带有ticket，此时应该与认证中心验证ticket(服务器之间直接发http)
     *
     * @param request
     * @return
     */
    @RequestMapping("/mainURL")
    public String returnURL(HttpServletRequest request, @RequestParam(name = "ticket", required = false) String ticket) {
        HttpSession localHttpSession = LocalSessions.getSession(LocalSessions.localSessionId);
        String returnURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        int endIndex = returnURL.length() - request.getRequestURI().length() + 1;
        if (localHttpSession.getAttribute("auth") != null) {//发现session中存有会话记录，则直接返回main页面
            return "main";
        }
        //session中没有会话记录，则重定向去认证中心。
        // 认证中心有两种可能：1)全局会话有记录，则令牌+重定向。2）全局无会话记录，则重定向到login页面，然后令牌+重定向。
        if (ticket != null) {
            localHttpSession.setAttribute("ticket", ticket);
            return authCheck(request, ticket);
        }
        return "redirect:"+serverUrlAddr+"/server/page/login?returnURL=" + returnURL + "&&endIndex=" + endIndex;
    }

    /**
     * 该方法通过httpclient调用/server/auth/verify，去认证中心验证ticket的真伪
     *
     * @param request 请求参数是/auth/check?ticket=xxxx;
     * @return
     */
    public String authCheck(HttpServletRequest request, String ticket) {
        HttpSession localHttpSession = LocalSessions.getSession(LocalSessions.localSessionId);
        //向认证中心发送验证token请求
        HttpResponse httpResponse = httpUtil.httpGet(serverUrlAddr+"/server/auth/verify?ticket=" + ticket);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        try {
            if (statusCode == HttpStatus.SC_OK) {
                String responseBody = null;
                responseBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                ObjectMapper objectMapper = new ObjectMapper();
                VerifyBean verifyResult = objectMapper.readValue(responseBody, VerifyBean.class);
//验证通过,应用返回浏览器需要验证的页面
                if (verifyResult.getRet().equals("0")) {
                    Auth auth = new Auth();
                    auth.setUserId(verifyResult.getUserId());
                    auth.setUsername(verifyResult.getUsername());
                    auth.setGlobalId(verifyResult.getGlobalId());
                    localHttpSession.setAttribute("auth", auth);
//建立本地会话
                    return "redirect:" + request.getRequestURI();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//解析json数据
        return "error";
    }

    /**
     * 若 浏览器发出，没有参数，则删除本地回话，并通知认证中心，同时返回登录页
     * 若 认证中心发出，带有 localId参数，则删除本地会话，返“ok”
     *
     * @param request
     * @return
     */
    @RequestMapping("/auth/logout")
    public String authLogout(HttpServletRequest request, @RequestParam(name = "globalId", required = false) String globalId) {
        HttpSession localHttpSession = LocalSessions.getSession(LocalSessions.localSessionId);
        String ticketId = (String) localHttpSession.getAttribute("ticket");
        TokenInfo tokenInfo = tokenUtils.getToken("ticketId_"+ticketId);
        localHttpSession.invalidate();
        if (globalId == null) {
            //通知认证中心要登出，加globalId参数
            String url = ""+serverUrlAddr+"/server/auth/logout?globalId=" + tokenInfo.getGlobalId();
            HttpResponse httpResponse = httpUtil.httpGet(url);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return "redirect:/client/mainURL";
            }
        }
        return "redirect:"+serverUrlAddr+"/server/page/login";
    }

}
