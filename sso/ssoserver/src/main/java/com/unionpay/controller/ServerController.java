package com.unionpay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionpay.info.TokenInfo;
import com.unionpay.info.User;
import com.unionpay.info.VerifyBean;
import com.unionpay.session.GlobalSessions;
import com.unionpay.util.HttpUtil;
import com.unionpay.util.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/server")
@Controller
public class ServerController {

    @Resource
    TokenUtils tokenUtils;
    @Resource
    HttpUtil httpUtil;

    @RequestMapping("/page/login")
    public String pageLogin(HttpServletRequest request, @RequestParam(name = "returnURL", required = false) String returnURL, @RequestParam(name = "endIndex", required = false) int endIndex) {
        // 从redis中获取该系统的ticket
        HttpSession globalHttpSession = GlobalSessions.getSession(GlobalSessions.globalSessionId);
        String ssoClient = returnURL!=null? returnURL.substring(0, endIndex): "";
        globalHttpSession.setAttribute("returnURL", returnURL);
        globalHttpSession.setAttribute("ssoClient", ssoClient);
        if (globalHttpSession.getAttribute("username")!=null) {//认证系统判断已经登陆
            String ticketId = UUID.randomUUID().toString().replace("-", "");
            globalHttpSession.setAttribute("ticket", ticketId);
            return "redirect:" + returnURL + "?ticket=" + ticketId;
        } else {//认证系统发现该用户未登陆
            return "login";
        }
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String authLogin(HttpServletRequest request, User user) {
        HttpSession globalHttpSession = GlobalSessions.getSession(GlobalSessions.globalSessionId);
        TokenInfo tokenInfo = new TokenInfo();
        String validateResult = validateAdmin(request, user);
        if (validateResult.equals("true")) {
            tokenInfo.setUsername(user.getUsername());
            tokenInfo.setGlobalId(GlobalSessions.globalSessionId);
            String ssoclient = (String) globalHttpSession.getAttribute("ssoClient");
            List<String> ssoClients = new ArrayList<String>();
            ssoClients.add(ssoclient);
            tokenInfo.setSsoClient(ssoClients);
            globalHttpSession.setAttribute("username",user.getUsername());
        }
        String ticketId = UUID.randomUUID().toString().replace("-", "");
        globalHttpSession.setAttribute("ticket", ticketId);
        tokenUtils.setToken("ticketId_"+ticketId, tokenInfo);
        return "redirect:" + globalHttpSession.getAttribute("returnURL") + "?ticket=" + ticketId;
    }

    @RequestMapping(value = "/auth/verify", method = RequestMethod.GET)
    @ResponseBody
    public String authVerify(HttpServletRequest request, @RequestParam(name = "ticket", required = false)String ticket, HttpServletResponse response) throws IOException {
        if (tokenUtils.getToken("ticketId_"+ticket)==null){
            return null;
        }
        VerifyBean verifyBean = new VerifyBean();
        verifyBean.setRet("0");
        verifyBean.setGlobalId(tokenUtils.getToken("ticketId_"+ticket).getGlobalId());
        verifyBean.setUsername(tokenUtils.getToken("ticketId_"+ticket).getUsername());
        String mapJakcson = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapJakcson = mapper.writeValueAsString(verifyBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mapJakcson;
    }

    @RequestMapping("/auth/logout")
    @ResponseBody
    public void authLogout(HttpServletRequest request, @RequestParam(name = "globalId", required = false)String globalId) {
        HttpSession globalHttpSession = GlobalSessions.getSession(GlobalSessions.globalSessionId);
        //清除全局会话，清除其他系统的本地会话
        String ticket = "ticketId_"+(String) globalHttpSession.getAttribute("ticket");
        List<String> ssoClients = tokenUtils.getToken(ticket).getSsoClient();
        if (GlobalSessions.getSession(globalId)!=null) {
            GlobalSessions.getSession(globalId).invalidate();
        }
        for (String ssoClient : ssoClients) {
            httpUtil.httpGet(ssoClient+"/client/auth/logout"+globalId);
        }
        return;
    }

    public String validateAdmin(HttpServletRequest request, User user) {
        return "true";
    }
}