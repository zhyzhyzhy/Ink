package org.ink.web.http;

import java.util.UUID;

/**
 *
 *
 * @author zhuyichen 2017-11-17
 */
public final class HttpKit {

    /**
     * create one unique sessionId
     * @return unique id
     */
    public static String createUniqueId() {

        //TODO

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
