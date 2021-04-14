package com.trs.bookstoreservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public String helloWorld() {
		logger.debug("Print Hello World");
		return "Hello World";
	}

}
