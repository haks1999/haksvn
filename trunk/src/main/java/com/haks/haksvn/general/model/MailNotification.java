package com.haks.haksvn.general.model;

import org.springframework.stereotype.Component;

import com.haks.haksvn.common.code.model.Code;

@Component
public class MailNotification {

	// transfer request
	// transfer request reject
	// transfer request approve
	// transferGroup transfer
	private Code notificationCode;
	private boolean useNotification;
	
}
