package org.seckill.exception;

/**
 * Created by weihuap on 2017/5/21.
 */
public class SecKillException extends RuntimeException {
    public SecKillException(String message) {
        super(message);
    }

    public SecKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
