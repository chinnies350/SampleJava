package com.demo.api;

public class PostStudentResult {
    private String outMessage;
    private Integer outStatusCode;

    // Default constructor
    public PostStudentResult() {
    }

	public PostStudentResult(String outMessage, Integer outStatusCode) {
		super();
		this.outMessage = outMessage;
		this.outStatusCode = outStatusCode;
	}

	public String getOutMessage() {
		return outMessage;
	}

	public void setOutMessage(String outMessage) {
		this.outMessage = outMessage;
	}

	public Integer getOutStatusCode() {
		return outStatusCode;
	}

	public void setOutStatusCode(Integer outStatusCode) {
		this.outStatusCode = outStatusCode;
	}

    
}

