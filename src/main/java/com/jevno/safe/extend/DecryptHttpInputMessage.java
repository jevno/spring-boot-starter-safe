package com.jevno.safe.extend;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;


public class DecryptHttpInputMessage implements HttpInputMessage {
	HttpHeaders headers;
	InputStream body;
	
	public DecryptHttpInputMessage(HttpHeaders headers, InputStream body) {
		this.headers = headers;
		this.body = body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}

	@Override
	public InputStream getBody() throws IOException {
		return body;
	}

}
