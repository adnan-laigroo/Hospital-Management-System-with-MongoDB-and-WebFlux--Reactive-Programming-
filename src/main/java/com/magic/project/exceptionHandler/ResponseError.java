package com.magic.project.exceptionHandler;

import java.util.List;

public class ResponseError {
	private String info;
	private List<String> messages;

	public ResponseError() {
	}

	public ResponseError(String info, List<String> messages) {
		this.info = info;
		this.messages = messages;
	}

	public ResponseError(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
