package com.hms.util.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

    private boolean success;
    private String message;

    @JsonAlias("data")
    private T result;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(true, "success", result);
    }

    public static <T> ApiResponse<T> success(String message, T result) {
        return new ApiResponse<>(true, message, result);
    }

    public static <T> ApiResponse<T> success(T result, String message) {
        return success(message, result);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> fail(String message, T result) {
        return new ApiResponse<>(false, message, result);
    }

    public static <T> ApiResponse<T> error(String message) {
        return fail(message);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, "success", null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return success("success", data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return success(message, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getData() {
        return result;
    }
}

