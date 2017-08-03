package com.noname.web.http;

public enum HttpHeader {

    ContentType("Content-Type");

    private String value;
    HttpHeader(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return this.value;
    }

}
