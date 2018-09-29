package com.unionpay.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LocalSessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LocalSessions.addSession(httpSessionEvent.getSession().getId(), httpSessionEvent.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        LocalSessions.delSession(httpSessionEvent.getSession().getId());
    }
}