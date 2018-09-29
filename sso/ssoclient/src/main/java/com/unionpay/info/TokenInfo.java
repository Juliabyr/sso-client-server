package com.unionpay.info;

import java.util.List;

public class TokenInfo {

    private int userId;//用户唯一标识ID
    private String username;//用户登陆名
    private List<String> ssoClient;//应用系统唯一标识
    private String globalId;//登陆成功全局会话sessionId

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getSsoClient() {
        return ssoClient;
    }

    public void setSsoClient(List<String> ssoClient) {
        this.ssoClient = ssoClient;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }
}
