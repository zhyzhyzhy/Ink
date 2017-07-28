package com.noname.security;

public class JwtInfo {

    private boolean isOk = false;

    public JwtInfo(boolean isOk) {
        this.isOk = isOk;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
