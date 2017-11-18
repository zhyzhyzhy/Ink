package org.ink.web.http;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhuyichen 2017-11-17
 */
public final class HttpKit {

    private static final SessionIdGenerator SESSION_ID_GENERATOR = new SessionIdGenerator();

    /**
     * create one unique sessionId
     *
     * @return unique id
     */
    public static String createUniqueId() {
        return SESSION_ID_GENERATOR.generateSessionId();
    }

}

/**
 * generate sessionid by SecureRandom
 */
class SessionIdGenerator {
    private ConcurrentLinkedQueue<SecureRandom> queue;
    private int length;

    SessionIdGenerator() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.length = 16;
    }

    public SessionIdGenerator(int length) {
        this.length = length;
    }

    private void getRandomBytes(byte[] bytes) {
        SecureRandom random = queue.poll();
        if (random == null) {
            random = new SecureRandom();
        }
        random.nextBytes(bytes);
        queue.add(random);
    }

    String generateSessionId() {

        byte random[] = new byte[16];

        StringBuilder buffer = new StringBuilder(2 * length);

        int resultLenBytes = 0;

        while (resultLenBytes < length) {
            getRandomBytes(random);
            for (int j = 0; j < random.length && resultLenBytes < length; j++) {
                byte b1 = (byte) ((random[j] & 0xf0) >> 4);
                byte b2 = (byte) (random[j] & 0x0f);
                if (b1 < 10) {
                    buffer.append((char) ('0' + b1));
                } else {
                    buffer.append((char) ('A' + (b1 - 10)));
                }
                if (b2 < 10) {
                    buffer.append((char) ('0' + b2));
                } else {
                    buffer.append((char) ('A' + (b2 - 10)));
                }
                resultLenBytes++;
            }
        }
        return buffer.toString();
    }
}