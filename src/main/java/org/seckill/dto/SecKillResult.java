package org.seckill.dto;

/**
 * Created by weihuap on 2017/5/28.
 */
//封装json结果
public class SecKillResult<T> {
    private Boolean success;
    private T data;
    private String error;

    public SecKillResult(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SecKillResult(Boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(String error) {
        this.error = error;
    }
}
