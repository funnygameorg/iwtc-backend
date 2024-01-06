package com.example.demo.common.web;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface CustomAuthentication {
	boolean required() default true;
}
