package com.noname.web.http;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by zhuyichen on 2017/7/13.
 */
public class Response {
    private Object responseEntity;
    private HttpResponseStatus responseStatus;

    public Response(Object responseEntity, HttpResponseStatus responseStatus) {
        this.responseEntity = responseEntity;
        this.responseStatus = responseStatus;
    }

    public Object getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(Object responseEntity) {
        this.responseEntity = responseEntity;
    }

    public HttpResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
