package com.example.demo.common.config.property.exception;

public class IncompleteBeanConfigurationException extends RuntimeException {
	public IncompleteBeanConfigurationException(String propertyClassName) {
		super(propertyClassName);
	}
}
