package org.ink.security;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.ink.security.exception.ForbiddenException;
import org.ink.security.exception.UnauthorizedException;

public class CheckResult {

    private boolean isOk = false;

    private HttpResponseStatus status;

    private Exception exception;

    public CheckResult(boolean isOk) {
        this.isOk = isOk;
    }

    public CheckResult(HttpResponseStatus status) {
        this.isOk = false;
        this.status = status;
        if (status.equals(HttpResponseStatus.UNAUTHORIZED)) {
            exception = new UnauthorizedException();
        }
        else if (status.equals(HttpResponseStatus.FORBIDDEN)) {
            exception = new ForbiddenException();
        }
    }

    public Exception exception() {
        return exception;
    }

    public boolean isOk() {
        return isOk;
    }

}
