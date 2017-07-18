package com.noname.web.http;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by zhuyichen on 2017/7/13.
 *
 * Response Builder
 * 建造者模式
 */
public class Response {
    private Object responseEntity;
    private HttpResponseStatus responseStatus;

    private Response(Object responseEntity, HttpResponseStatus responseStatus) {
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

    static public class Builder {

        private Object responseEntity = null;
        private HttpResponseStatus responseStatus = HttpResponseStatus.OK;
        public Builder entity(Object responseEntity) {
            this.responseEntity = responseEntity;
            return this;
        }
        public Builder status(HttpResponseStatus responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }
        public Response build() {
            return new Response(responseEntity, responseStatus);
        }

    }

}
