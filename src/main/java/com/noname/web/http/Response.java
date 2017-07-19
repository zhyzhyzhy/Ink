package com.noname.web.http;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.BatchUpdateException;

/**
 * Created by zhuyichen on 2017/7/13.
 * <p>
 * Response Builder
 * 建造者模式
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

    public static Builder ok() {
        return status(HttpResponseStatus.OK);
    }

    public static Builder badRequest() {
        return status(HttpResponseStatus.BAD_REQUEST);
    }


    public static Builder status(HttpResponseStatus status) {
        return new Builder(status);
    }

    public static class Builder {
        private HttpResponseStatus status = HttpResponseStatus.OK;
        private Object object;
        public Builder(HttpResponseStatus status) {
            this.status = status;
        }

        public Builder body(Object o) {
            this.object = o;
            return this;
        }
        public Response build() {
            return new Response(object, status);
        }
    }


}
