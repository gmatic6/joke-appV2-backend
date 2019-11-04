package com.combis.jokeApp.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PerformanceLogger {
	
	private Logger log;
	
	PerformanceLogger() {
		log = LoggerFactory.getLogger(PerformanceLogger.class);
	}
	public void info(String message) {
		log.info(message);
	}
	public void error(String message) {
		log.error(message);
	}
	public void warn(String message) {
		log.warn(message);
	}
	public void trace(String message) {
		log.trace(message);
	}
	public void debug(String message) {
		log.debug(message);
	}
}
