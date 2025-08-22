package com.example.fie_service.enums;

public enum Errors implements HasResult{
    FILE_UPLOAD_FAIL(-1001, "FILE COULD NOT BE UPLOADED"),
    FILE_DOWNLOAD_FAIL(-1002, "FILE COULD NOT DOWNLOAD"),
    FILE_DELETE_FAIL(-1005, "FILE_DELETE_FAIL"),
    INVALID_BUCKET_NAME(-1003, "INVALID BUCKET NAME");

    private final String message;
    private final Integer code;

    Errors(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
