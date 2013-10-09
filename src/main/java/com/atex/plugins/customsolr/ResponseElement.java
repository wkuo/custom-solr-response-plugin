package com.atex.plugins.customsolr;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import com.atex.plugins.customsolr.util.ResponseUtil;
import com.polopoly.common.lang.StringUtil;

public class ResponseElement 
{
    private static final Logger LOG = Logger.getLogger(ResponseElement.class.getName());
    protected static final String RESPONSE = "response";
    protected static final String RESULT = "result";
    protected static final String STATUS = "status";
    protected static final String CODE = "code";
    protected static final String MESSAGE = "message";


    private Element response ;

    public ResponseElement() {
        response = new Element(RESPONSE);
        Element result = new Element(RESULT);
        Element status = new Element(STATUS);
        response.addContent(result);
        response.addContent(status);
    }

    public void addResult(Element child) {
        getResult().addContent(child);
    }

    public Element getResponse() {
        return response;
    }

    public Element getResult() {
        return getResponse().getChild(RESULT);
    }

    public Element getStatus() {
        return getResponse().getChild(STATUS);
    }

    public void setStatusMsg(String msg) {
        setStatusMsg(null, msg);
    }

    public void setStatusMsg(int code, String msg) {
        setStatusMsg(String.valueOf(code), msg);
    }

    protected void setStatusMsg(String code, String msg) {
        Element status = getStatus();
        if (code!=null && !StringUtil.isEmpty(code)) {
            Element codeElement = new Element(CODE);
            codeElement.setText(code);
            status.addContent(codeElement);
        }
        if (msg!=null && !StringUtil.isEmpty(msg)) {
            Element msgElement = new Element(MESSAGE);
            msgElement.setText(msg);
            status.addContent(msgElement);
        }
    }

    public void write(HttpServletResponse resp) {
        ResponseUtil.write(resp, getResponse());
    }
}
