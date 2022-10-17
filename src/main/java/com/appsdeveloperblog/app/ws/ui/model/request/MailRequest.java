package com.appsdeveloperblog.app.ws.ui.model.request;

import lombok.Data;

@Data
public class MailRequest {
	
	private String firstName;
	private String lastName;
	private String to;
	private String from;
	private String subject;

}
