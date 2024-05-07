package com.tencent.wxcloudrun.config;



import com.tencent.wxcloudrun.enums.ResponseEnum;

import java.io.Serializable;

/**
 * 返回体封装
 *
 * @author dongdongxie
 * @date 2024/05/14
 */
public class ResponseMessage<T> implements Serializable {

    private static final long serialVersionUID = -5782205017337866984L;

    /**
     * 是否调用成功
     */
    private boolean success;

    /**
     * code码
     */
    private int code;

    /**
     * 提示信息
     */
    private String errorMsg;

    private Long total;

    private Long pageNum;

    private Long pageSize;

    private T data;

    public ResponseMessage() {
    }

    public ResponseMessage(boolean success, T data, Integer code, String message) {
        this.code = code;
        this.data = data;
        this.errorMsg = message;
        this.success = success;
    }

    public ResponseMessage(boolean success, T data, Integer code, String message, Long total, Long pageNum, Long pageSize) {
        this.code = code;
        this.data = data;
        this.errorMsg = message;
        this.success = success;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> ResponseMessage<T> success(String message) {
        return new ResponseMessage<T>(true, null, ResponseEnum.SUCCESS.getValue(), message);
    }

    public static <T> ResponseMessage<T> success() {
        return new ResponseMessage<>(true, null, ResponseEnum.SUCCESS.getValue(), ResponseEnum.SUCCESS.getName());
    }

    public static <T> ResponseMessage<T> success(ResponseEnum responseEnum) {
        return new ResponseMessage<>(true, null, responseEnum.getValue(), responseEnum.getName());
    }

    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(true, data, ResponseEnum.SUCCESS.getValue(), ResponseEnum.SUCCESS.getName());
    }

    public static <T> ResponseMessage<T> success(ResponseEnum responseEnum, T data) {
        return new ResponseMessage<>(true, data, responseEnum.getValue(), responseEnum.getName());
    }

    public static <T> ResponseMessage<T> success(T data, long total, long pageNum, long pageSize) {
        return new ResponseMessage<>(true, data, ResponseEnum.SUCCESS.getValue(), ResponseEnum.SUCCESS.getName(), total, pageNum, pageSize);
    }


    public static <T> ResponseMessage<T> fail() {
        return new ResponseMessage<>(false, null, ResponseEnum.SYSTEM_CODE.getValue(), ResponseEnum.SYSTEM_CODE.getName());
    }

    public static <T> ResponseMessage<T> fail(ResponseEnum responseEnum) {
        return new ResponseMessage<>(false, null, responseEnum.getValue(), responseEnum.getName());
    }

    public static <T> ResponseMessage<T> fail(T data) {
        return new ResponseMessage<>(false, data, ResponseEnum.SYSTEM_CODE.getValue(), ResponseEnum.SYSTEM_CODE.getName());
    }

    public static <T> ResponseMessage<T> fail(String message) {
        return new ResponseMessage<>(false, null, ResponseEnum.SYSTEM_CODE.getValue(), message);
    }

    public static <T> ResponseMessage<T> fail(Integer code, String message) {
        return new ResponseMessage<>(false, null, code, message);
    }

    public static <T> ResponseMessage<T> fail(ResponseEnum responseEnum, T data) {
        return new ResponseMessage<>(false, data, responseEnum.getValue(), responseEnum.getName());
    }

    public static <T> ResponseMessage<T> fail(ResponseEnum responseEnum, String message) {
        return new ResponseMessage<>(false, null, responseEnum.getValue(), message);
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
