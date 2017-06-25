package org.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by weihuap on 2017/5/21.
 */
public class SecKillCloseExecution extends SecKillException{


    public SecKillCloseExecution(String message) {
        super(message);
    }

    public SecKillCloseExecution(String message, Throwable cause) {
        super(message, cause);
    }
}
