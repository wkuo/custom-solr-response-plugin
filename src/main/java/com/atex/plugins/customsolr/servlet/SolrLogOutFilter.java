package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.atex.plugins.customsolr.ResponseElement;
import com.polopoly.user.server.Caller;
import com.polopoly.user.server.PermissionDeniedException;
import com.polopoly.user.server.jsp.UserFactory;

public class SolrLogOutFilter 
    implements Filter
{

    private static final Logger LOG = Logger.getLogger(SolrLogOutFilter.class.getName());

    FilterConfig config;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        ResponseElement root = new ResponseElement();
        try {
            UserFactory userFactory = UserFactory.getInstance();
            Object[] userObj = userFactory.getUserAndCallerIfPresent(req, resp);
            Caller caller = null;
            if (userObj!=null && userObj[1] instanceof Caller) {
                caller = (Caller) userObj[1];
            }
            if (caller!=null && caller.getSessionKey()!=null) {
                userFactory.logoutUser(req, resp);
                root.setStatusMsg(HttpStatus.SC_OK, "Log out successfully.");
            } else {
                root.setStatusMsg("The session was previously logged out.");
            }
        } catch (PermissionDeniedException e) {
            root.setStatusMsg(HttpStatus.SC_FORBIDDEN, "Permission denied.");
            LOG.log(Level.WARNING, "Error during logout.", e);
        }
        root.write(resp);
    }

}
