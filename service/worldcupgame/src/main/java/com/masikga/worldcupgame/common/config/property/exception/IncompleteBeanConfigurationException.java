package com.masikga.worldcupgame.common.config.property.exception;

public class IncompleteBeanConfigurationException extends RuntimeException {
	public IncompleteBeanConfigurationException(String propertyClassName) {
		super(propertyClassName);
	}
}
