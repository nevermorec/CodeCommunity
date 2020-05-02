package com.community.community.exception;

public class CustomizeException extends RuntimeException {
	private String message;
	private Integer code;

	public CustomizeException(ICustomizeErrorCode errorCode) {
		this.code = errorCode.getErrorCode();
		this.message = errorCode.getErrorMessage();
	}

	@Override
	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		return code;
	}
}
